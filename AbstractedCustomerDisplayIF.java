//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  抽象カスタマディスプレイインターフェースクラス。
 *  実際のカスタマディスプレイと仮想カスタマディスプレイの共通のインターフェース，データ構造，振舞いを定義する。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

/*
 *  抽象カスタマディスプレイインターフェースクラス
 */

public abstract class AbstractedCustomerDisplayIF {
    /*
     *  カスタマディスプレイのライン表示を左寄せ，センタリング，右寄せのいずれにするかを指定する列挙型
     */

    public enum Alignment {
        // 左寄せ
        LEFT,
        // センタリング
        CENTERING,
        // 右寄せ
        RIGHT,
    }


    /*
     *  コンストラクタ。
     *  抽象クラスのインスタンスが作られることはないのでアクセス可能性を protected としている。
     */

    protected AbstractedCustomerDisplayIF() {
        // do nothing
    }


    /*
     *  カスタマディスプレイの表示をクリアする。
     */

    abstract public void clear();


    /*
     *  カスタマディスプレイの上ラインにメッセージ message を表示する。
     *  左寄せ，センタリング，右寄せは alignment で指定する。
     */

    abstract public void displayUpperMessage(String message, Alignment alignment);


    /*
     *  カスタマディスプレイの下ラインにメッセージ message を表示する。
     *  左寄せ，センタリング，右寄せは alignment で指定する。
     */

    abstract public void displayLowerMessage(String message, Alignment alignment);
}
