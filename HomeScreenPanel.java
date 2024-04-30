//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  ホーム画面クラス。ユーザはホーム画面で POS 端末の機能選択を行う。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;


/*
 *  ホーム画面クラス
 */

public class HomeScreenPanel extends JPanel implements ActionListener {
    /*
     *  ホーム画面クラスの唯一のインスタンス（Singleton パターン）
     */

    private static HomeScreenPanel theHomeScreenPanel = null;


    /*
     *  ウィジェット
     */
    // 商品チェックボタン
    private JButton checkArticlesButton = null;

    // 会員管理ボタン
    private JButton memberManagementButton = null;


    /*
     *  コンストラクタ
     */

    private HomeScreenPanel() {
        // レイアウトマネージャを無効化する。
        setLayout(null);

        // 商品チェックボタンを生成する。
        checkArticlesButton = new JButton("商品チェック");
        checkArticlesButton.setBounds(240, 270, 144, 100);
        checkArticlesButton.addActionListener(this);
        checkArticlesButton.setActionCommand("checkArticles");
        add(checkArticlesButton);

        // 会員検索ボタンを生成する。
        memberManagementButton = new JButton("会員管理");
        memberManagementButton.setBounds(440, 270, 144,100);
        memberManagementButton.addActionListener(this);
        memberManagementButton.setActionCommand("memberManagement");
        add(memberManagementButton);
    }


    /*
     *  ホーム画面の唯一のインスタンスを返す。（Singleton パターン）
     */

    public static HomeScreenPanel getInstance() {
        if (theHomeScreenPanel == null) {
            theHomeScreenPanel = new HomeScreenPanel();
        }
        return theHomeScreenPanel;
    }


    /*
     *  ホーム画面上のボタンが押されるとこのメソッドが呼び出される。
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("checkArticles")) {
            // 商品チェックボタンが押され，商品のチェックが指示された。
            // POS端末全体制御に商品チェックの開始を要求する。
            POSTerminalApp.getInstance().checkArticlesRequested();
        } else if (cmd.equals("memberManagement")) {
            // 会員検索ボタンが押され，会員検索が指示された。
            // POS端末全体制御に会員管理の開始を要求する。
            POSTerminalApp.getInstance().memberManagementRequested();
        }
    }
}
