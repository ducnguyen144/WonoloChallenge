package wonolo.sgeoi.Interface;

import java.util.ArrayList;

import wonolo.sgeoi.DataApp.PostOfMediaSearchData;
import wonolo.sgeoi.DataApp.UserData;

public interface ITransferDataListener {
    // set/get information of my account
    void setUserInformation(UserData user);

    // set/get information all post around location
    void setUserInformationMediaSearch(ArrayList<PostOfMediaSearchData> list);
}

