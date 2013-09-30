package com.humbughq.mobile.test.mutated;

import java.util.ArrayList;
import java.util.List;

import com.humbughq.mobile.HumbugActivity;
import com.humbughq.mobile.Message;
import com.humbughq.mobile.MessageListFragment;
import com.humbughq.mobile.MessageRange;
import com.humbughq.mobile.HumbugActivity.LoadPosition;

public class FakeAsyncGetOldMessages extends
        com.humbughq.mobile.AsyncGetOldMessages {
    public String calculatedResult;
    public int fmAnchor;
    public int fmNumBefore;
    public int fmNumAfter;
    public boolean shouldFmSucceed;
    public boolean fmCalled;
    public List<Message> appendTheseMessages;
    public List<FakeAsyncGetOldMessages> recurseRequestsReceived;
    private MessageListFragment myfragment;

    public FakeAsyncGetOldMessages(MessageListFragment fragment) {
        super(fragment);
        myfragment = fragment;
        fmCalled = false;
        shouldFmSucceed = false;
        recurseRequestsReceived = new ArrayList<FakeAsyncGetOldMessages>();
        filter = null;
    }

    public void executeBasedOnPresetValues() {
        // LP doesn't matter, so just go with INITIAL
        this.execute(fmAnchor, HumbugActivity.LoadPosition.INITIAL,
                fmNumBefore, fmNumAfter, filter);
    }

    @Override
    protected boolean fetchMessages(int anchor, int num_before, int num_after,
            String[] params) {
        fmCalled = true;
        fmAnchor = anchor;
        fmNumBefore = num_before;
        fmNumAfter = num_after;
        if (appendTheseMessages != null) {
            this.receivedMessages.addAll(appendTheseMessages);
        }
        return shouldFmSucceed;
    }

    @Override
    protected void recurse(LoadPosition position, int amount, MessageRange rng,
            int anchor) {
        FakeAsyncGetOldMessages task = new FakeAsyncGetOldMessages(myfragment);
        task.rng = rng;
        if (position == HumbugActivity.LoadPosition.ABOVE) {
            task.fmNumBefore = amount;
        } else {
            task.fmNumAfter = amount;
        }
        task.fmAnchor = anchor;
        recurseRequestsReceived.add(task);
    }

    @Override
    protected void onPostExecute(String result) {
        calculatedResult = result;
    }

}
