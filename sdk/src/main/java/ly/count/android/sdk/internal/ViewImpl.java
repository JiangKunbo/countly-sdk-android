package ly.count.android.sdk.internal;

import ly.count.android.sdk.Event;
import ly.count.android.sdk.Session;
import ly.count.android.sdk.View;

/**
 * View implementation for Countly Views plugin
 */

class ViewImpl implements View {
    static final String EVENT = "[CLY]_view";
    static final String NAME = "name";
    static final String VISIT = "visit";
    static final String VISIT_VALUE = "1";
    static final String SEGMENT = "segment";
    static final String SEGMENT_VALUE = "Android";
    static final String START = "start";
    static final String START_VALUE = "1";
    static final String EXIT = "exit";
    static final String EXIT_VALUE = "1";
    static final String BOUNCE = "bounce";
    static final String BOUNCE_VALUE = "1";

    private String name;
    private Session session;
    private Event start;
    private boolean firstView;
    private boolean started, ended;

    ViewImpl(Session session, String name) {
        this.name = name;
        this.session = session;
    }

    @Override
    public void start(boolean firstView) {
        if (started) {
            return;
        }
        this.started = true;
        this.firstView = firstView;

        start = session.event(EVENT).addSegments(NAME, this.name,
                VISIT, VISIT_VALUE,
                SEGMENT, SEGMENT_VALUE);

        if (firstView) {
            start.addSegment(START, START_VALUE);
        }

        start.record();
    }

    @Override
    public void end(boolean lastView) {
        if (ended) {
            return;
        }
        ended = true;

        EventImpl event = (EventImpl) session.event(EVENT).addSegments(NAME, this.name,
                SEGMENT, SEGMENT_VALUE);

        event.setDuration(Device.uniqueTimestamp() - event.getTimestamp());

        if (lastView) {
            event.addSegment(EXIT, EXIT_VALUE);
        }

        if (lastView && firstView) {
            event.addSegment(BOUNCE, BOUNCE_VALUE);
        }

        event.record();
    }
}
