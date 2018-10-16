package com.ig.eval.controller

import com.ig.eval.configuration.ApplicationProperties
import spock.lang.Specification

class DefaultControllerSpec extends Specification {

    private DefaultController unit
    def welcomeMessage = "Welcome to Rob's Coffee house"

    void setup() {
        unit = new DefaultController()
        unit.applicationProperties = Mock(ApplicationProperties)
        unit.applicationProperties.getWelcome() >> welcomeMessage
    }

    def "display welcome message"() {
        expect:
        unit.displayWelcomeMessage() == welcomeMessage
    }
}
