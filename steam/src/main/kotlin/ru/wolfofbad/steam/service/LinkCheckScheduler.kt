package ru.wolfofbad.steam.service

import org.apache.logging.log4j.LogManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class LinkCheckScheduler(
    private val linkService: LinkService,
) {
    private val logger = LogManager.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "#{schedulerInterval}")
    fun check() {
        logger.info("Checking link updates")
        linkService.checkUpdates()
    }
}
