//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  抽象キャッシュドロワインターフェースクラス。
 *  実際のキャッシュドロワと仮想のキャッシュドロワの共通のインターフェース，データ構造，振舞いを定義する。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

/*
 *  抽象キャッシュドロワインターフェースクラス
 */

public abstract class AbstractedCashDrawerIF {
    /*
     *  コンストラクタ。
     *  抽象クラスのインスタンスが作られることはないのでアクセス可能性を protected としている。
     */

    protected AbstractedCashDrawerIF() {
        // do nothing
    }


    /*
     *  キャッシュドロワを開ける。
     */

    abstract void openDrawer();


    /*
     *  キャッシュドロワを閉める。
     */

    abstract void closeDrawer();
}
