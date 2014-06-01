import editor.res.Conversor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        
        assertEquals("1","7e",c.decToHexa("126"));
        assertEquals("97", c.decToHexa("-105"));
        assertEquals("0", c.decToHexa("0"));
        assertEquals("54", c.decToHexa("2.5"));
        assertEquals("d4", c.decToHexa("-2.5"));
        assertEquals("7e", c.decToHexa("15.2"));
        assertEquals("fe", c.decToHexa("-15.2"));
        assertEquals("1", c.decToHexa("0.07"));
        assertEquals("81", c.decToHexa("-0.07"));
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

}
