package ru.wolfofbad.authorization.service

import ru.wolfofbad.authorization.dto.request.bot.LinkRequest
import ru.wolfofbad.authorization.dto.request.bot.ListLinkRequest

interface LinkService {
    fun handleLink(request: LinkRequest)

    fun getLinks(request: ListLinkRequest)
}
