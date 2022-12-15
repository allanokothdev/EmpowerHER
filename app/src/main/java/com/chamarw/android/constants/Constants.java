package com.chamarw.android.constants;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Constants {

    public static final String PASSCODE = "PASSCODE";
    public static final String DEFAULT_PASSCODE = "0000";
    public final static String KEY_STORE_REFERENCE = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public static final String TRANSFORMATION = "AES/GCM/NoPadding";

    public static final int DEPOSIT_TX = 0;

    public static final String MAPS_KEY = "AIzaSyByvCOPBbvJifstuN2AiGH1uWAkc_c8lNI";

    public static final String GROUP_TOKENS = "groupTokens";
    public static final String TOKENS = "tokens";
    public static final String TOKEN = "token";
    public static final String CHAMAS = "chamas";
    public static final String LISTINGS = "listings";
    public static final String USERS = "users";
    public static final String MEMBERS = "members";
    public static final String TRANSACTIONS = "transactions";

    public final static String OBJECT = "object";
    public final static String OBJECT_ID = "objectID";
    public final static String OBJECT_LIST = "objectList";

    public static final Double MATIC_USD = 0.0;

    public static final String NAME = "name";
    public static final String PIC = "pic";
    public static final String ADDRESS = "Address";
    public static final String PRIVATE_KEY = "Private_Key";
    public static final String CHAMA = "Chama";

    public static final String CREATE_WALLET = "createWallet";
    public static final String CREATE_LISTING = "createListing";
    public static final String FETCH_LISTINGS = "fetchListings";
    public static final String DELETE_LISTING = "deleteListing";

    public static final String FETCH_MEMBERS = "fetchMembers";
    public static final String ADD_MEMBER = "fetchMember";
    public static final String DELETE_MEMBER = "deleteMember";

    public static final String CREATE_CHAMA = "createChama";
    public static final String FETCH_CHAMAS = "fetchChamas";
    public static final String FETCH_CHAMA = "fetchChama";
    public static final String DELETE_CHAMA = "deleteChama";

    public static final String DEPOSIT_MONEY = "depositMoney";
    public static final String UPDATE_SAVINGS = "updateSavings";
    public static final String TRANSFER_ASSET = "transferAsset";
    public static final String FETCH_OWNERSHIP = "fetchOwnership";

    public final static String MATIC_BALANCE = "MATIC_BALANCE";
    public final static String SAVINGS = "SAVINGS";

}
