//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  仮想プリンタインターフェースクラス。
 *  実際のプリンタと同一のインターフェースを持ち，実際のプリンタの挙動を PC 上でシミュレーションする。
 *  これにより開発時に POS 端末の実機がなくてもアプリケーションの振舞いを確認できる。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.awt.*;
import javax.swing.*;


/*
 *  仮想プリンタインターフェースクラス
 */

public class VirtualPrinterIF extends AbstractedPrinterIF {
    /*
     *  唯一のインスタンスへの参照（Singleton パターン）
     */
    private static VirtualPrinterIF thePrinter = null;

    /*
     *  ウィジェット
     */
    // ウィンドウフレーム
    private JFrame printerFrame = null;

    // スクロールペイン
    private JScrollPane scrollPane = null;

    // テキスト
    private JTextArea paperArea = null;

    /*
     *  コンストラクタ
     */

    private VirtualPrinterIF() {
        // フレームを生成する。
        printerFrame = new JFrame("プリンタ");
        printerFrame.setBounds(4, 28, 500, 500);
        printerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        printerFrame.setResizable(false);
        printerFrame.getContentPane().setLayout(new BorderLayout());

        // 印刷面を表現するテキストエリアを生成する。
        paperArea = new JTextArea();
        paperArea.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
        paperArea.setEditable(false);

        // 印刷面を表現するテキストエリアを含むスクロールペインを生成する。
        scrollPane = new JScrollPane(paperArea);

        // スクロールペインをフレームに追加する。
        printerFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // フレームを可視にする。
        printerFrame.setVisible(true);
    }


    /*
     *  仮想プリンタの唯一のインスタンスを返す。（Singleton パターン）
     */

    public static AbstractedPrinterIF getInstance() {
        if (thePrinter == null) {
            thePrinter = new VirtualPrinterIF();
        }
        return thePrinter;
    }


    /*
     *  仮想プリンタを初期化する。
     */

    public void initialize() {
        paperArea.setText(" ");
    }


    /*
     *  文字列 s を印刷する。
     *  改行はしない。
     */

    public void print(String s) {
        paperArea.append(s);
    }


    /*
     *  文字列 s を印刷し，改行する。
     */

    public void printLine(String s) {
        paperArea.append(s);
    }


    /*
     *  整数 n を印刷する。
     *  改行はしない。
     */

    public void print(int n) {
        paperArea.append(Integer.toString(n));
    }


    /*
     *  整数 n を width 桁で印刷する。
     *  改行はしない。
     */

    public void print(int n, int width) {
        String s = Integer.toString(n);
        if (s.length() > width) {
            for (int i = 0; i < width; i++) paperArea.append("*");
        } else {
            for (int i = 0; i < width - s.length(); i++) paperArea.append(" ");
            paperArea.append(s);
        }
    }
}
