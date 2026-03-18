# Parley Bot

A Telegram bot for synchronous multilingual group chat. Each participant sets the language they want to read in, and the bot silently delivers a translation of every message addressed to them — without interrupting the conversation flow.

## How it works

Add the bot to a group. Each person runs `/lang` once to declare the language they want to read in. From that point on, every message is automatically translated and re-posted for anyone whose target language differs from the original.

**Example**

| User | Studies | Sets language |
|------|---------|---------------|
| Alice | Spanish | `/lang ES` |
| Bob | English | `/lang EN` |
| Carol | Russian | `/lang RU` |

When Alice sends `"hola, ¿cómo están?"` in Spanish, Parley detects that Bob wants English and Carol wants Russian, and posts two translations. Alice sees nothing extra because the message was already in her language.

A user can watch several languages at once: `/lang EN,ES` subscribes to both.

## Commands

| Command | Description |
|---------|-------------|
| `/lang [codes]` | Set your target language(s). Comma-separated. Example: `/lang EN,ES` |
| `/voice` | Reply to any message to receive an audio pronunciation of it |
| `/explain` | Reply to any message to get a brief grammar and vocabulary explanation |
| `/exit` | Unsubscribe from translations in this chat |
| `/help` | List supported languages |
| `/feedback [text]` | Send a bug report or suggestion |

## Supported languages

`EN` English · `ES` Spanish · `FR` French · `DE` German · `RU` Russian · `IT` Italian · `JA` Japanese · `GE` Georgian

## Edge cases

**No duplicate translations.** Before posting, the bot runs a three-stage heuristic to verify that the output is actually a different language from the input: a fast Unicode-script check, a Jaccard token-overlap score, and a cosine similarity of character-frequency distributions. If all three agree that nothing changed, the translation is suppressed. This handles common cases such as technical terms, loanwords, and short messages that are identical in two languages.

**Mixed-language messages.** If a message contains a significant mix of scripts or borrows heavily from another language, the heuristic may produce a false negative (no translation sent) or a false positive (translation sent when unnecessary). This is a known limitation.

**Voice messages.** Incoming voice messages are transcribed with Whisper and then go through the normal translation pipeline. If transcription fails or produces an empty result, the sender receives an error message instead of silent failure.

**Language code validation.** `/lang` silently discards unrecognised codes and saves only the valid ones. Sending `/lang EN,INVALID,ES` sets English and Spanish. If no valid code is found, an error lists the accepted values.

**User removal.** `/exit` and physically leaving the group both trigger deletion of the user's language preferences from the chat. The two events are handled by the same code path.

## Architecture

The service follows **hexagonal architecture** (ports and adapters).

```
Telegram API
    │
    ▼
BotUpdatesListener          ← entry point; routes each update to matching handlers
    │
    ▼
TelegramUpdateHandler       ← adapter layer; one implementation per command/event
    │
    ▼
Port In (interface)         ← application boundary; defines what the app can do
    │
    ▼
Application Service         ← business logic; depends only on port interfaces
    │
    ▼
Port Out (interface)        ← defines what the app needs from the outside world
    │
    ▼
Infrastructure adapters     ← OpenAI, Telegram file API, SQLite
```

**Key components**

| Layer | Responsibility |
|-------|---------------|
| `adapter/telegram/handler/` | Dispatch Telegram updates to the right use case |
| `application/service/` | Orchestrate translation, language setting, user stats |
| `port/input/` | Contracts called by adapters into the application |
| `port/output/` | Contracts the application calls into infrastructure |
| `infrastructure/translation/` | OpenAI Responses API (GPT-4.1 Nano) |
| `infrastructure/voice/` | OpenAI TTS (GPT-4o Mini) and Whisper STT (GPT-4o Mini) |
| `infrastructure/comparator/` | Language-sameness heuristic |
| `infrastructure/database/` | SQLite via Spring Data JPA |

**Tech stack**

- Kotlin + Spring Boot 3
- SQLite (via `sqlite-jdbc` + Hibernate community dialect)
- OpenAI Java SDK
- `java-telegram-bot-api` (pengrad)
- Spring Retry for transient OpenAI/Telegram failures
- Spring Actuator + Prometheus metrics

## Running locally

```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
# fill in bot.token and open-ai.api-key
./gradlew bootRun
```

Or with Docker Compose:

```bash
TELEGRAM_BOT_TOKEN=... OPENAI_API_KEY=... docker compose up
```

## Development

```bash
./gradlew build          # compile + test
./gradlew test           # tests only
./gradlew check          # lint (kotlinter)

# run a single test class
./gradlew test --tests "com.kvsiniuk.parleybot.application.service.TranslationProcessingServiceTest"
```
