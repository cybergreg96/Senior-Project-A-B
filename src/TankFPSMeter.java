/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import java.util.concurrent.TimeUnit;

// FPSMeter is used to report the number of frames calculated in the last second to stderr.

class TankFPSMeter 
{
    private static final long SECOND = TimeUnit.SECONDS.toNanos(1);
    private long framesInSecond = 0;
    private long nextSecond = 0;

    void handle(final long nanos) 
    {
        framesInSecond++;
        if (nextSecond == 0) 
        {
            nextSecond = nanos + SECOND;
        } 
        else if (nanos >= nextSecond) 
        {
            nextSecond = nanos + SECOND;
            framesInSecond = 0;
        }
    }
}
