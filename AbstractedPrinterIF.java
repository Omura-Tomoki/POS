//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  抽象プリンタインターフェースクラス。
 *  実際のプリンタと仮想のプリンタの共通のインターフェース，データ構造，振舞いを定義する。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

/*
 *  抽象プリンタインターフェースクラス
 */

public abstract class AbstractedPrinterIF {
    /*
     *  コンストラクタ。
     *  抽象クラスのインスタンスが作られることはないのでアクセス可能性を protected としている。
     */

    protected AbstractedPrinterIF() {
    }


    /*
     *  プリンタを初期化する。
     */

    public abstract void initialize();


    /*
     *  文字列 s を印刷する。
     */

    public abstract void print(String s);


    /*
     *  整数 n を印刷する。
     */

    public abstract void print(int n);


    /*
     *  整数 n を width 桁で印刷する。
     */

    public abstract void print(int n, int width);
}
