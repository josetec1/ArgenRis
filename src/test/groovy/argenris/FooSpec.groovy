package argenris


import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class FooSpec extends Specification implements DomainUnitTest<Foo> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == true
    }
}
