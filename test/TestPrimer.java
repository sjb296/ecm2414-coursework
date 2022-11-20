import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCard.class,
        TestCardGame.class,
        TestCardPackReader.class,
        TestDeck.class,
        TestOutputFileHelper.class,
        TestPlayer.class
})
public class TestPrimer {
}