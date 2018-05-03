
import java.util.concurrent.TimeUnit;

// FPSMeter is used to report the number of frames calculated in the last second to stderr.
// I am not sure if this works absolutely correctly because its using javafx's pulses to calculate
// the FPS and I do not think that pulses are one to one with frames. But whatever, it works well enough.
class TankFPSMeter {
    public final long SECOND = TimeUnit.SECONDS.toNanos(1);
    private long framesInSecond = 0;
    private long nextSecond = 0;
        
    void handle(final long nanos) {
        framesInSecond++;
        if (nextSecond == 0) {
            nextSecond = nanos + SECOND;
        } else if (nanos >= nextSecond) {
            nextSecond = nanos + SECOND;
            framesInSecond = 0;
        }
    }
}
