package de.melsicon.examples

import org.apache.kafka.streams.kstream.ValueJoiner
import spock.lang.Specification
import static de.melsicon.examples.CustomValueJoiner.createJoiner;
class CustomValueJoinerTest extends Specification{

    def "testifValuesAreMergedCorrectly"(){
        given:
        var a="12"
        var b="3"
        ValueJoiner valueJoiner = createJoiner()

        when:
        var c=valueJoiner.apply(a,b)

        then:
        c!=15.0
        c!=15
        c!="15"
        c=="15.0"
    }


    def "testifExceptionIsThrownUponWrongInputType"(){
        given:
        var a="12"
        var b=3
        ValueJoiner valueJoiner = createJoiner()

        when:
        valueJoiner.apply(a,b)

        then:
        thrown ClassCastException
    }
}
