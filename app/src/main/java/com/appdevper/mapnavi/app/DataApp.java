package com.appdevper.mapnavi.app;

import com.appdevper.mapnavi.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worawit on 2/21/16.
 */
public class DataApp {

    private static List<Place> placeList;

    public static List<Place> getPlaceList() {
        return placeList;
    }


    public static List<Place> getPlaceList(String type) {
        List<Place> placeListType = new ArrayList<>();
        for (Place place : getPlaceList()) {
             if(type.equals(place.getType())){
                 placeListType.add(place);
             }
        }
        return placeListType;
    }

    private static List<Place> init() {
        List<Place> list = new ArrayList<>();

        list.add(new Place("ตลาดน้ำอัมพวา", "", 13.425510, 99.955297, "S"));
        list.add(new Place("อุทยาน รัชกาลที่ 2", "", 13.426498, 99.952553, "S"));
        list.add(new Place("ตลาดน้ำท่าคา", "", 13.471899, 99.995321, "S"));
        list.add(new Place("ตลาดร่มหุบ", "", 13.407558, 99.999987, "S"));
        list.add(new Place("ตลาดน้ำบางน้อย", "", 13.462223, 99.944317, "S"));
        list.add(new Place("ศูนย์อนุรักษ์แมวไทยโบราณ", "", 13.431221, 99.947109, "S"));
        list.add(new Place("ศูนย์อนุรักษ์ป่าชายเลน", "", 13.331619, 99.968842, "S"));
        list.add(new Place("ศาลกรมหลวงชุมพรเขตอุดมศักดิ์", "", 13.362221, 100.022614, "S"));
        list.add(new Place("ดอนหอยหลอด", "", 13.361733, 100.023098, "S"));

        list.add(new Place("วัดบางแคน้อย", "", 13.433333, 99.947491, "B"));
        list.add(new Place("วัดเพชรสมุทรวรวิหาร", "", 13.409418, 99.998993, "B"));
        list.add(new Place("วัดศรัทธาธรรม", "", 13.378561, 99.994885, "B"));
        list.add(new Place("วัดอัมพวาเจติยาราม", "", 13.426475, 99.953800, "B"));
        list.add(new Place("วัดอินทราราม", "", 13.440644, 99.920620, "B"));
        list.add(new Place("วัดทุ่งเศรษฐี", "", 13.432587, 99.903991, "B"));
        list.add(new Place("วัดคลองโคน", "", 13.335117, 99.965323, "B"));
        list.add(new Place("วัดจุฬามณี", "", 13.429042, 99.965460, "B"));
        list.add(new Place("วัดพญาญาติ (วัดปากง่าม)", "", 13.424855, 99.971625, "B"));
        list.add(new Place("วัดบางแคใหญ่", "", 13.429840, 99.945964, "B"));
        list.add(new Place("วัดบางเกาะเทพศักดิ์", "", 13.439593, 99.937345, "B"));
        list.add(new Place("ค่ายบางกุ้ง", "", 13.445150, 99.941339, "B"));

        list.add(new Place("โรงแรม อัมพวาน่านอน แอนด์ สปา", "", 13.425752, 99.956878, "H"));
        list.add(new Place("ชูชัยบุรี ศรีอัมพวา", "", 13.427182, 99.960633, "H"));
        list.add(new Place("กระดังงารีสอร์ท2 อัมพวา", "", 13.424855, 99.971625, "H"));
        list.add(new Place("บ้านพี่อุ้ม เรือนพี่ต๋อง อัมพวารีสอร์ท", "", 13.419752, 99.958671, "H"));
        list.add(new Place("บ้านตุ่มรีสอร์ท", "", 13.403874, 99.980071, "H"));
        list.add(new Place("เดอะเกรซ อัมพวา รีสอร์ท", "", 13.402246, 99.980382, "H"));
        list.add(new Place("บ้านตุ่ม อัมพวา", "", 13.426134, 99.957698, "H"));

        list.add(new Place("ร้านแดงอาหารทะเล(เจ้าเก่า)", "", 13.387993, 99.988209, "F"));
        list.add(new Place("ร้านอาหารริมเขื่อน", "", 13.368059, 99.955129, "F"));
        list.add(new Place("ร้านอาหารเคียงน้ำ", "", 13.363202, 99.948336, "F"));
        list.add(new Place("ร้านเกษร", "", 13.334793, 99.964840, "F"));
        list.add(new Place("ครัวบางตะบูน (ลุงญา)", "", 13.265163, 99.939627, "F"));

        return list;
    }
}
