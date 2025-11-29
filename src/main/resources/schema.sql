CREATE TABLE IF NOT EXISTS user
(
    id            TEXT PRIMARY KEY,
    user_id       INTEGER NOT NULL,
    message_count INTEGER NOT NULL DEFAULT 0,
    voice_count   INTEGER NOT NULL DEFAULT 0,
    explain_count INTEGER NOT NULL DEFAULT 0,
    created_date  TEXT    NOT NULL,
    updated_date  TEXT    NOT NULL,
    version       INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_users_user_id ON user (user_id);

CREATE TABLE IF NOT EXISTS user_chat
(
    id           TEXT PRIMARY KEY,
    user_id      INTEGER NOT NULL,
    chat_id      INTEGER NOT NULL,
    languages    TEXT    NOT NULL,
    created_date TEXT    NOT NULL,
    updated_date TEXT    NOT NULL,
    version      INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_users_chat_id ON user_chat (user_id);
CREATE INDEX IF NOT EXISTS idx_users_chat_id ON user_chat (chat_id);
