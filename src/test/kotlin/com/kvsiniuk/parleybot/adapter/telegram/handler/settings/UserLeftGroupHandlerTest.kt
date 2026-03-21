package com.kvsiniuk.parleybot.adapter.telegram.handler.settings

import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.DeleteUserChatPortIn
import com.kvsiniuk.parleybot.port.input.model.DeleteUserChatRequest
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserLeftGroupHandlerTest {
    private val deletePort = mockk<DeleteUserChatPortIn>(relaxed = true)
    private val handler = UserLeftGroupHandler(deletePort)

    // ── canApply ───────────────────────────────────────────────────────────

    @Test
    fun `applies when user physically left the group`() {
        assertTrue(handler.canApply(update(userLeftGroup = true)))
    }

    @Test
    fun `applies when user sends the EXIT command`() {
        assertTrue(handler.canApply(update(message = "/exit")))
    }

    @Test
    fun `does NOT apply for SET_LANG command - regression for previous bug`() {
        // Previously canApply() mistakenly used BotCommand.SET_LANG instead of EXIT,
        // which caused every /lang message to also trigger user deletion.
        assertFalse(handler.canApply(update(message = "/lang")))
    }

    @Test
    fun `does not apply for ordinary text messages`() {
        assertFalse(handler.canApply(update(message = "hello there")))
    }

    @Test
    fun `does not apply when message is null and user did not leave`() {
        assertFalse(handler.canApply(update()))
    }

    // ── process ───────────────────────────────────────────────────────────

    @Test
    fun `deletes the user's chat record when they leave`() {
        handler.process(update(userId = 99L, userLeftGroup = true))

        verify { deletePort.deleteUserChat(DeleteUserChatRequest(userId = 99L)) }
    }

    @Test
    fun `deletes the user's chat record when they send the EXIT command`() {
        handler.process(update(userId = 55L, message = "/exit"))

        verify { deletePort.deleteUserChat(DeleteUserChatRequest(userId = 55L)) }
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun update(
        message: String? = null,
        userId: Long = 1L,
        userLeftGroup: Boolean = false,
    ) = TelegramUpdateMessage(
        message = message,
        userId = userId,
        userLeftGroup = userLeftGroup,
        language = null,
        voiceFileId = null,
    )
}
