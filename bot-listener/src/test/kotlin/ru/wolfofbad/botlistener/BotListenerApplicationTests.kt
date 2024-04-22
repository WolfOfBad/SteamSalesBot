package ru.wolfofbad.botlistener

import org.junit.jupiter.api.Test
import ru.wolfofbad.botlistener.bot.linkcheck.LinkChecker
import ru.wolfofbad.botlistener.bot.linkcheck.SteamLinkParser

class BotListenerApplicationTests {

    @Test
    fun contextLoads() {
        val chain = LinkChecker.LinkCheckerManager.link(
            SteamLinkParser()
        )

        val first = "https://store.steampowered.com/app/881100/Noita/"
        val second = "store.steampowered.com/app/881100/Noita/"
        val third = ""

        println(chain.parse(first))
        println(chain.parse(second))
        println(chain.parse(third))

        println("xd")
    }

    @Test
    fun xd() {
        val regex = "^(https://)?store\\.steampowered\\.com/app/.*".toRegex()
        val str = "https://store.steampowered.com/app/881100/Noita/"

        val match = regex.find(str)

        println(match?.value)
    }

}
