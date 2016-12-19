package in.vmc.mcubeconnect.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import in.vmc.mcubeconnect.model.Model;
import in.vmc.mcubeconnect.model.OptionsData;
import in.vmc.mcubeconnect.model.SiteData;
import in.vmc.mcubeconnect.model.UserData;
import in.vmc.mcubeconnect.model.VisitData;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;

/**
 * Created by gousebabjan on 30/3/16.
 */
public class Parser implements TAG {


    public static Model ParseData(JSONObject response) throws JSONException {
        JSONObject mediaArray = null;
        JSONObject data;
        Model model = null;
        try {
            model = new Model();
            if (response.has(CODE)) {
                model.setCode(response.getString(CODE));
            }
            if (response.has(MESSAGE)) {

                model.setMessage(response.getString(MESSAGE));
            }

            if (response.has(DESC)) {

                model.setDescription(response.getString(DESC));
            }
            if (response.has(NAME)) {

                model.setName(response.getString(NAME));
            }

            if (response.has(LOGO)) {

                model.setLogo(response.getString(LOGO));
            }
            if (response.has(NUMBER)) {
                model.setPhone(response.getString(NUMBER).equals("null") ? "08043445150" : response.getString(NUMBER));
            }
            if (response.has(SITEID)) {
                model.setSiteId(response.getString(SITEID));
            }
            if (response.has(BID)) {
                model.setBid(response.getString(BID));
            }
            if (response.has(LIKES)) {
                String Like = response.getString(LIKES);
                if (Like.equals("1")) {
                    model.setLike(true);
                } else {
                    model.setLike(false);
                }
            }

            if (response.has(BEACONID)) {
                model.setBeacinId(response.getString(BEACONID));
            }
            if (response.has(MEDIA))

                try {
                    mediaArray = response.getJSONObject(MEDIA);
                    ArrayList<String> images = new ArrayList<String>();

                    if (mediaArray.has(IMAGE_1)) {
                        images.add(mediaArray.getString(IMAGE_1));
                    }
                    if (mediaArray.has(IMAGE_2)) {
                        images.add(mediaArray.getString(IMAGE_2));
                    }
                    if (mediaArray.has(IMAGE_3)) {
                        images.add(mediaArray.getString(IMAGE_3));
                    }
                    if (mediaArray.has(IMAGE_4)) {
                        images.add(mediaArray.getString(IMAGE_4));
                    }
                    if (mediaArray.has(VIDEO)) {
                        String Vedio = mediaArray.getString(VIDEO);
                        if (Vedio.length() > 2) {
                            model.setVedioUrl(Utils.extractYTId(mediaArray.getString(VIDEO)));
                        }
                    }

                    model.setImages(images);
                } catch (Exception e) {

                }

            if (response.has(DATA))

                try {
                    data = response.getJSONObject(DATA);
                    Iterator keys = data.keys();
                    ArrayList<OptionsData> optionsDatas = new ArrayList<OptionsData>();
                    while (keys.hasNext()) {
                        OptionsData optionsData = new OptionsData();
                        String currentKey = (String) keys.next();
                        optionsData.setOptionName(data.getString(currentKey));
                        optionsDatas.add(optionsData);
                        // do something here with the value...
                    }
                    model.setOptionsData(optionsDatas);


                } catch (Exception e) {

                }


        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return model;

    }

    public static Model ParseLocationData(JSONObject response) throws JSONException {
        Model model = new Model();
        JSONObject mediaArray = null;

        try {
            if (response.has(CODE)) {
                model.setCode(response.getString(CODE));
            }
            if (response.has(MESSAGE)) {
                model.setMessage(response.getString(MESSAGE));
            }
            if (response.has(DATA)) {
                JSONObject data = response.getJSONObject(DATA);
                if (data.has("lname")) {
                    model.setName(data.getString("lname"));
                }
                if (data.has(BEACONID)) {
                    model.setBeacinId(data.getString(BEACONID));
                }
                if (data.has(SITEID)) {
                    model.setSiteId(data.getString(SITEID));
                }
                if (data.has(LOGO)) {
                    model.setLogo(data.getString(LOGO));
                }
                if (data.has(NUMBER)) {
                    model.setPhone(data.getString(NUMBER).equals("null") ? "08043445150" : data.getString(NUMBER));


                }

                if (data.has(BID)) {
                    model.setBid(data.getString(BID));
                }


                if (data.has(DESC)) {
                    model.setDescription(data.getString(DESC));
                }

                if (data.has(LIKES)) {
                    String Like = data.getString(LIKES);
                    if (Like.equals("1")) {
                        model.setLike(true);
                    } else {
                        model.setLike(false);
                    }
                }
                if (data.has(MEDIA)) {

                    try {
                        mediaArray = data.getJSONObject(MEDIA);
                        ArrayList<String> images = new ArrayList<String>();

                        if (mediaArray.has(IMAGE_1)) {
                            images.add(mediaArray.getString(IMAGE_1));
                        }
                        if (mediaArray.has(IMAGE_2)) {
                            images.add(mediaArray.getString(IMAGE_2));
                        }
                        if (mediaArray.has(IMAGE_3)) {
                            images.add(mediaArray.getString(IMAGE_3));
                        }
                        if (mediaArray.has(IMAGE_4)) {
                            images.add(mediaArray.getString(IMAGE_4));
                        }
                        if (mediaArray.has(VIDEO)) {
                            String Vedio = mediaArray.getString(VIDEO);
                            if (Vedio.length() > 2) {
                                model.setVedioUrl(Utils.extractYTId(mediaArray.getString(VIDEO)));
                            }
                        }

                        model.setImages(images);


                    } catch (Exception e) {
                    }

                }

            }


        } catch (Exception e) {

        }
        return model;
    }

    public static ArrayList<SiteData> ParseSiteData(JSONObject response) throws JSONException {

        SiteData siteData = null;
        JSONArray data = null;
        ArrayList<SiteData> siteDatas = new ArrayList<>();
        ;
        if (response.has(DATA)) {
            data = response.getJSONArray(DATA);
            for (int i = 0; i < data.length(); i++) {
                JSONObject currentlocation = data.getJSONObject(i);
                siteData = new SiteData();
                if (currentlocation.has(ID)) {
                    siteData.setId(currentlocation.getString(ID));
                }
                if (currentlocation.has(BID)) {
                    siteData.setBid(currentlocation.getString(BID));
                }
                if (currentlocation.has(NAME)) {
                    siteData.setName(currentlocation.getString(NAME));
                }
                siteDatas.add(siteData);

            }
        }
        return siteDatas;
    }

    public static UserData ParseUserData(JSONObject response) throws JSONException {
        UserData userData = new UserData();
        if (response.has(DATA)) {
            JSONObject data = response.getJSONObject(DATA);


            if (data.has(EMAIL)) {
                userData.setEmail(data.getString(EMAIL));

            }

            if (data.has(USERNAME)) {
                userData.setUsername(data.getString(USERNAME));

            }

            if (data.has(PASSWORD)) {
                userData.setPassword(data.getString(PASSWORD));

            }

            if (data.has(DOB)) {
                userData.setDob(data.getString(DOB));

            }
            if (data.has(IMAGE_1)) {
                userData.setImage(data.getString(IMAGE_1));

            }
            if (data.has(GENDER)) {
                userData.setGender(data.getString(GENDER));
            }


        }
        return userData;
    }

    public static ArrayList<String> ParseImages(JSONObject response) throws JSONException {
        ArrayList<String> images = null;
        JSONObject imagesData = null;
        if (response.has(DATA))

            try {
                imagesData = response.getJSONObject(DATA);
                Iterator keys = imagesData.keys();
                images = new ArrayList<String>();
                while (keys.hasNext()) {

                    String currentKey = (String) keys.next();
                    images.add(imagesData.getString(currentKey));

                    // do something here with the value...
                }


            } catch (Exception e) {

            }
        return images;
    }

    public static ArrayList<VisitData> ParseVisitData(JSONObject response,boolean isVisit) throws JSONException {

        VisitData visitData = null;
        JSONArray data = null;
        ArrayList<VisitData> visitDatas  = new ArrayList<VisitData>();
        if (response.has(DATA)) {
            visitDatas = new ArrayList<>();
            data = response.getJSONArray(DATA);
            for (int i = 0; i < data.length(); i++) {
                JSONObject currentvisit = data.getJSONObject(i);
                visitData = new VisitData();
                visitData.setDelete(isVisit);
                if (currentvisit.has(SITENAME)) {
                    visitData.setSitename(currentvisit.getString(SITENAME));
                }
                if (currentvisit.has(SITEID)) {
                    visitData.setSiteid(currentvisit.getString(SITEID));
                }
                if (currentvisit.has(SITEDESC)) {
                    visitData.setSitedesc(currentvisit.getString(SITEDESC));
                }
                if (currentvisit.has(SITEICON)) {
                    visitData.setSiteicon(currentvisit.getString(SITEICON));
                    visitData.setBitmapLogp(JSONParser.getBitmapFromURL(visitData.getSiteicon()));
                }
                if (currentvisit.has(NUMBER)) {
                    visitData.setNumber(currentvisit.getString(NUMBER));
                }
                if (currentvisit.has(BID)) {
                    visitData.setBid(currentvisit.getString(BID));
                }


                if (currentvisit.has(OFFER_PER)) {
                    visitData.setOffer(currentvisit.getString(OFFER_PER));
                }
                if (currentvisit.has(PROPERTY_NAME)) {
                    visitData.setPropertyname(currentvisit.getString(PROPERTY_NAME));
                }
                if (currentvisit.has(OFFER_DEC)) {
                    visitData.setOffer_desc(currentvisit.getString(OFFER_DEC));
                }
                if (currentvisit.has(LIKES)) {
                    String Like = currentvisit.getString(LIKES);
                    if (Like.equals("1")) {
                        visitData.setLike(true);
                    } else {
                        visitData.setLike(false);
                    }
                }

                visitDatas.add(visitData);

            }
        }
        return visitDatas;
    }


}



