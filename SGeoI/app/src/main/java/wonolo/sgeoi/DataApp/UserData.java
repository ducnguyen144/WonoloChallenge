package wonolo.sgeoi.DataApp;

import java.util.ArrayList;

public class UserData {

    private String mAvatarUser;
    private String mUsername;
    private String mFullname;
    private String mUserID;
    private String mFollows;
    private String mFollowed_by;
    private String mMedia;

    public ArrayList<String> getArrayUserData(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(mAvatarUser);
        arrayList.add(mUsername);
        arrayList.add(mFullname);
        arrayList.add(mUserID);
        arrayList.add(mFollows);
        arrayList.add(mFollowed_by);
        arrayList.add(mMedia);
        return arrayList;
    }

    public String getmMedia() {
        return mMedia;
    }

    public void setmMedia(String mMedia) {
        this.mMedia = mMedia;
    }

    public String getmAvatarUser() {
        return mAvatarUser;
    }

    public void setmAvatarUser(String mAvatarUser) {
        this.mAvatarUser = mAvatarUser;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmFullname() {
        return mFullname;
    }

    public void setmFullname(String mFullname) {
        this.mFullname = mFullname;
    }

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getmFollows() {
        return mFollows;
    }

    public void setmFollows(String mFollows) {
        this.mFollows = mFollows;
    }

    public String getmFollowed_by() {
        return mFollowed_by;
    }

    public void setmFollowed_by(String mFollowed_by) {
        this.mFollowed_by = mFollowed_by;
    }





}
