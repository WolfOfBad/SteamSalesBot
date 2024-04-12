package ru.wolfofbad.botlistener.bot.linkcheck

import java.net.URI

abstract class LinkChecker {
    private var next: LinkChecker? = null

    abstract fun parse(uri: String): URI?

    protected fun parseNext(uri: String): URI? {
        return next?.parse(uri)
    }

    protected fun parseRegex(uri: String, regex: Regex): String? {
        val match = regex.find(uri) ?: return null

        var result = match.value
        if (match.groups[1] == null) {
            result = "https://$result"
        }

        return result
    }

    companion object LinkCheckerManager {
        fun link(first: LinkChecker, vararg chain: LinkChecker): LinkChecker {
            var head = first
            for (node in chain) {
                head.next = node
                head = node
            }

            return first
        }
    }
}
