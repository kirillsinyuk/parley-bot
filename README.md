# Parley bot

This is a Telegram bot for synchronous chat translation. 
It helps you practice a language with your friends even when none of them know this language. 

## Functionality description

All you need to do is to add bot to a group, select target language(`/lang` command) and start sending messages. 
This bot will translate your messages to languages of your interlocutors and vice versa.

For example:<br>
There is a group with 3 users: User1, User2, User3.<br>
User1: Studies Spanish.<br>
User2: Studies English.<br>
User3: Studies Russian.<br>

All users selected target language as the language they study.<br>
When User1 sends a message in Spanish, this bot will check target languages for users of the rest of the group and translate message to these languages. In this cage, bot will send 2 translations: in English and Russian.<br>
**!NB** If user sends message using other language, that matches other user language, there will be no translation. But this bot can mismatch or be inaccurate in some complex cases with mixed languages or loanwords.

This bot also translates voice messages and generate voice from text message(`/voice` command), so you know how to pronounce right.

Users can leave a feedback about problems or bugs with `/feedback` command.<br>
`/explain` command gives a brief vocabulary and grammar explanation.

## Tech description

This service uses Kotlin and hexagonal architecture.<br>
For integration with Telegram this service uses `com.github.pengrad:java-telegram-bot-api`



