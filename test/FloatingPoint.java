
import domain.ALU;
import static domain.ALU.ALU;
import editor.res.Conversor;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class FloatingPoint {

    public FloatingPoint() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void puntoFlotante() {

        Conversor c = new Conversor();

        assertEquals("1", "7e", c.decToHexa("126"));
        assertEquals("97", c.decToHexa("-105"));
        assertEquals("0", c.decToHexa("0"));
        assertEquals("54", c.decToHexa("2.5"));
        assertEquals("d4", c.decToHexa("-2.5"));
        assertEquals("7e", c.decToHexa("15.2"));
        assertEquals("fe", c.decToHexa("-15.2"));
        assertEquals("1", c.decToHexa("0.07"));
        assertEquals("81", c.decToHexa("-0.07"));
        assertEquals("cc", c.decToHexa("-1.75"));
        assertEquals("40", c.decToHexa("0.0"));
        assertEquals("40", c.decToHexa("-0.0"));
    }

    @Test
    public void addFloat() {

        ALU c = ALU.ALU();
        
        Byte b1 = (byte)0x81;
        Byte b2 = (byte)0x54;
        assertEquals("1", "54", Integer.toHexString(c.addFloat(b2,b1)));
        b1 = (byte)0x54;
        b2 = (byte)0xCC;
        assertEquals("1", "38", Integer.toHexString(c.addFloat(b1,b2)));
        b1 = (byte)0x01;
        b2 = (byte)0x95;
        assertEquals("1", "8a", Integer.toHexString(0xFF&c.addFloat(b1,b2)));
        b1 = (byte)0x01;
        b2 = (byte)0x15;
        assertEquals("1", "1d", Integer.toHexString(0xFF&c.addFloat(b1,b2)));
        b1 = (byte)0x53;
        b2 = (byte)0x70;
        assertEquals("1", "74", Integer.toHexString(0xFF&c.addFloat(b1,b2)));
        b1 = (byte)0x0;
        b2 = (byte)0x0;
//        assertEquals("1", "0", Integer.toHexString(0xFF&c.addFloat(b1,b2)));

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

}
