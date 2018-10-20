package vse_vrut.com.geoquiz;

/**
 * Created by Влад on 21.10.2017.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
//    private boolean isCheat;

    public Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId;
        this.mAnswerTrue = mAnswerTrue;
//        this.isCheat = isCheat();
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

//    public boolean isCheat() {
//        return isCheat;
//    }

//    public void setCheat(boolean cheat) {
//        isCheat = cheat;
//    }
}
