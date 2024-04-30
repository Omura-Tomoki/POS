//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  ある一種類の「商品」を表す実体クラス。その「商品」の商品コード，名称，単価を記録する。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

/*
 *  商品クラス
 */

public class Article {
    /*
    　*  内部データ
     */
    // 商品コード
    private String articleCode = null;

    // 商品名
    private String articleName = null;

    // 定価
    private int cataloguePrice = 0;


    /*
     *  コンストラクタ
     */

    public Article(String _articleCode, String _articleName, int _cataloguePrice) {
        articleCode = _articleCode;
        articleName = _articleName;
        cataloguePrice = _cataloguePrice;
    }


    /*
     *  当該商品の商品コードを取得する。
     */

    public String getArticleCode() {
        return articleCode;
    }


    /*
     *  当該商品の商品名を取得する。
     */

    public String getArticleName() {
        return articleName;
    }


    /*
     *  当該商品の定価を取得する。
     */

    public int getCataloguePrice() {
        return cataloguePrice;
    }
}
