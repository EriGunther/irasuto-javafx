package application;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class fieldTest {

    @Test
    void fieldCreation1() {
        field testField1 = new field("B2");
        Assert.assertEquals(2, testField1.getNumber());
        Assert.assertEquals(fieldColor.black, testField1.getColor());
    }
    @Test
    void fieldCreation2() {
        field testField1 = new field("W3");
        Assert.assertEquals(3, testField1.getNumber());
        Assert.assertEquals(fieldColor.white, testField1.getColor());
    }

}