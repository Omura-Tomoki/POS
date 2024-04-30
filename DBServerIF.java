//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  外部データベースにインターフェースするクラス。Singleton パターンで実装している。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.ac.fukuoka_u.tl.NanakumaPOS.Member.Gender;


/*
 *  データベースサーバインターフェースクラス
 */

public class DBServerIF {
    /*
     *  データベースサーバインターフェースクラスの唯一のインスタンス（Singleton パターン）
     */

    private static DBServerIF theDBServerIF = null;


    /*
     *  内部データ
     */
    // データベース接続
    private Connection conn = null;

    // データベース接続先 URL
    private String url = "jdbc:mysql://vhost.cx.tl.fukuoka-u.ac.jp:13306/nanakumapos3";

    // データベースユーザ名
    private String user = "b3pbl";

    // データベースパスワード
    private String password = "nanakumapbl";

    /*
     *  データベースインターフェースに関する障害を通知する例外クラス
     */

    public class DBServerIFException extends Exception {
        /*
         *  コンストラクタ
         */
        public DBServerIFException(String _message) {
            super(_message);
        }
    }


    /*
     *  コンストラクタ
     */

    private DBServerIF () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
     *  データベースインターフェースの唯一のインスタンスを返す。（Singleton パターン）
     */

    public static DBServerIF getInstance() {
        if (theDBServerIF == null) {
            theDBServerIF = new DBServerIF();
        }
        return theDBServerIF;
    }


    /*
     *  商品コード articleCode の商品を検索する。
     */

    public Article findArticle(String articleCode) throws DBServerIFException {
        Article article = null;

        try {
            int count;
            Statement stmt = conn.createStatement();
            String sql = "select * from articletbl where code = '" + articleCode + "';";
            ResultSet rs = stmt.executeQuery(sql);
            count = 0;
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                article = new Article(code, name, price);
                count++;
            }
            if (count < 1) {
                throw new DBServerIFException("この商品はデータベースに登録されていません。");
            }
            if (count > 1) {
                throw new DBServerIFException("この商品はデータベースに重複登録されています。");
            }
            rs.close();
        }
        catch (SQLException ex) {
            throw new DBServerIFException("SQLException: " + ex.getMessage());
        }
        return article;
    }


    /*
     *  会員情報の登録を受け付ける。
     *  会員情報 member のとおりにデータベースに登録する。
     */

    public void registerMember(Member member) throws DBServerIFException {
        //@@@ 未実装
        //todo
        try{
            String gender = member.getGender().equals(Gender.Male) ? "m" : "f";
            String sql = "insert into membertbl values('%s', '%s', '%s', '%s', 0);".formatted(member.getID(), member.getName(), member.getFurigana(), gender);
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
            if(rs != 1){
                throw new DBServerIFException("メンバーの登録に失敗しました。");
            }
        }catch (SQLException e){
            throw new DBServerIFException("SQLException: " + e.getMessage());
        }
    }


    /*
     *  会員番号 membershipID の会員を検索する。
     */

    public Member findMember(String membershipID) throws DBServerIFException {
        Member member = null;

        try {
            int count;
            Statement stmt = conn.createStatement();
            String sql = "select * from membertbl where id='" + membershipID + "';";
            ResultSet rs = stmt.executeQuery(sql);
            count = 0;
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String furigana = rs.getString("furigana");
                String mf = rs.getString("gender");
                Gender gender;
                if (mf.equals("m")) {
                    gender = Gender.Male;
                } else if (mf.equals("f")) {
                    gender = Gender.Female;
                } else {
                    gender = null;
                }
                member = new Member(id, name, furigana, gender);
                count++;
            }
            if (count < 1) {
                throw new DBServerIFException("この会員はデータベースに登録されていません。");
            }
            if (count > 1) {
                throw new DBServerIFException("この会員はデータベースに重複登録されています。");
            }
            rs.close();
        }
        catch (SQLException ex) {
            throw new DBServerIFException("SQLException: " + ex.getMessage());
        }
        return member;
    }


    /*
     *  当該会員情報の変更要求を受け付ける。
     *  会員情報 member のとおりにデータベースを更新する。
     */

    public void updateMember(Member member) throws DBServerIFException {
        //@@@ 未実装
        //todo
        //aaaaaaaaa

        try{
            String gender = member.getGender().equals(Gender.Male) ? "m" : "f";
            String sql = "update membertbl set name='%s',furigana='%s',gender='%s' where id='%s';".formatted(member.getName(),member.getFurigana(),gender,member.getID());
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
            if(rs != 1){
                throw new DBServerIFException("失敗");
            }else{
                System.out.println(rs);
            }
        }

        catch (SQLException e){
            throw new DBServerIFException("SQLException:" + e.getMessage());
        }
    }


    /*
     *  会員番号 memberID の会員を削除する。
     */

    public void deleteMember(String memberID) throws DBServerIFException {
        //@@@ 未実装
        //@@@ 削除する会員の購入履歴も削除しなければならないことに注意。
        //todo



        try{
//            String id = member.getID(); id='" + membershipID + "';";
            String sql = "delete from membertbl where id = '" + memberID + "';";
            String sql1 = "delete from salestbl where member_id='" + memberID + "';";  //失敗した場合 id='memberID';のように＝の前後のスペースを消す。
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
            int rs1 = stmt.executeUpdate(sql1);
            if(rs != 1){
                throw new DBServerIFException("メンバーの削除に失敗しました。");
            }
        }
        catch (SQLException e) {
            throw new DBServerIFException("SQLException: " + e.getMessage());
        }
    }

    /*
     *  会員番号 memberID の会員の保有ポイントを,amount分加算する.
     */
    public void addPointAmount(String memberID, int amount) throws DBServerIFException{
        try{
            String sql = "update membertbl set point=point+%d where id='%s';".formatted(amount, memberID);
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
            if(rs != 1){
                throw new DBServerIFException("ポイントの加算処理に失敗しました。");
            }
        }catch (SQLException e) {
            throw new DBServerIFException("SQLException: " + e.getMessage());
        }
    }


    /*
     *  会員番号 memberID の会員の保有ポイントを,amount減少させる.
     * 減算時にゼロを下回るケースは考慮し射ていないので呼び出し側で対処する
     */
    public void decreasePointAmount(String memberID, int amount) throws DBServerIFException{
        try{
            String sql = "update membertbl set point=point-%d where id='%s';".formatted(amount, memberID);
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
            if(rs != 1){
                throw new DBServerIFException("ポイントの減算処理に失敗しました。");
            }
        }catch (SQLException e) {
            throw new DBServerIFException("SQLException: " + e.getMessage());
        }
    }
    /*
     *  会員番号 memberID の会員の現在の保有ポイントを返す
     */

    public int getCurrentPoint(String memberID) throws DBServerIFException {
        int currentPoint = 0;
        try {
            String sql = "select point from membertbl where id='%s';".formatted(memberID);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                String pointStr = rs.getString("point");
                currentPoint = Integer.parseInt(pointStr);
                count++;
            }
            if (count != 1) {
                throw new DBServerIFException("会員情報を正常に取得できませんでした。");
            }
        } catch (SQLException e) {
            throw new DBServerIFException("SQLException: " + e.getMessage());
        }
        return currentPoint;
    }
}

