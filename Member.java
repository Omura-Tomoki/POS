//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  あるひとりの「会員」を表すクラス。その「会員」の会員番号，氏名，ふりがな，性別を記録する。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

/*
 *  会員クラス
 */

public class Member {
    /*
     *  会員の性別を表す列挙型
     */

    public enum Gender {
        // 男性
        Male,
        // 女性
        Female,
    }


    /*
     *  内部データ
     */
    // 会員番号
    private String id = null;

    // 氏名
    private String name = null;

    // ふりがな
    private String furigana = null;

    // 性別
    private Gender gender = null;

    // 保有ポイント
    private int point = 0;


    /*
     *  コンストラクタ
     */

    public Member(String _id, String _name, String _furigana, Gender _gender) {
        id = _id;
        name = _name;
        furigana = _furigana;
        gender = _gender;
    }

    /*
     *  当該会員の会員番号を取得する。
     */

    public String getID() {
        return id;
    }

    /*
     *  当該会員の会員番号を設定する。
     */

    public void setID(String _id) {
        id = _id;
    }


    /*
     *  当該会員の氏名を取得する。
     */

    public String getName() {
        return name;
    }


    /*
     *  当該会員の氏名を設定する。
     */

    public void setName(String _name) {
        name = _name;
    }


    /*
     *  当該会員のフリガナを取得する。
     */

    public String getFurigana() {
        return furigana;
    }


    /*
     *  当該会員のフリガナを設定する。
     */

    public void setFurigana(String _furigana) {
        furigana = _furigana;
    }


    /*
     *  当該会員の性別を取得する。
     */

    public Gender getGender() {
        return gender;
    }


    /*
     *  当該会員の性別を設定する。
     */

    public void setGender(Gender _gender) {
        gender = _gender;
    }

    /*
     *  当該会員の保有ポイントを取得する。
     */

    public int getPoint() throws DBServerIF.DBServerIFException {

        return DBServerIF.getInstance().getCurrentPoint(id);
    }

    /*
     *  当該会員の保有ポイントを設定する。
     */

    public void setPoint(int _point) {
        point = _point;
    }


}
