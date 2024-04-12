package ru.wolfofbad.botlistener.service.linkcheck

import java.net.URI

class SteamLinkParser : LinkChecker() {
    private val regex = "^(https://)?store\\.steampowered\\.com/app/.*".toRegex()

    override fun parse(uri: String): URI? {
        val parseResult: String = parseRegex(uri, regex) ?: return parseNext(uri)

        return URI(parseResult)
    }
}
