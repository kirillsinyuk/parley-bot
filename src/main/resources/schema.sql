CREATE TABLE IF NOT EXISTS users (
     id TEXT PRIMARY KEY,
     user_id INTEGER NOT NULL,
     chat_id INTEGER NOT NULL,
     language TEXT,
     version INTEGER NOT NULL DEFAULT 0,
     created_date TEXT NOT NULL,
     updated_date TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_users_chat_id ON users(chat_id);
CREATE INDEX IF NOT EXISTS idx_users_user_id ON users(user_id);