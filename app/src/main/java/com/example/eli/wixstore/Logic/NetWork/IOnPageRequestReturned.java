package com.example.eli.wixstore.Logic.NetWork;

import java.util.List;

/**
 * Created by Eli on 14/10/2016.
 */

public interface IOnPageRequestReturned {
    void onCallSucceeded(List<ProductData> products);
    void onCallFailed();
}
