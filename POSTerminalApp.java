//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  POS端末全体制御クラス。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import jp.ac.fukuoka_u.tl.NanakumaPOS.CheckArticlesScreenPanel.CheckArticlesScreenPanelState;
import jp.ac.fukuoka_u.tl.NanakumaPOS.MemberManagementScreenPanel.MemberManagementScreenPanelState;


/*
 *  POS 端末全体制御クラス
 */

public class POSTerminalApp {
    /*
     *  唯一のインスタンスへの参照（Singleton パターン）
     */
    private static POSTerminalApp theApp = null;


    /*
     *  ウィジェット
     */
    // ウィンドウフレーム
    private JFrame frame = null;

    // キャプションラベル
    private JLabel captionLabel = null;

    // ウィンドウフレームに貼りつくベースのパネル
    private JPanel basePanel = null;

    // ベースパネル上のウィジェットのレイアウトを司るレイアウトマネージャ
    private CardLayout cardLayout = null;


    /*
     *  プログラムのエントリポイント
     *  この関数からプログラムが開始される。
     */

    public static void main(String args[]) {
        theApp = new POSTerminalApp();
        theApp.run();
    }


    /*
     *  コンストラクタ
     */

    private POSTerminalApp() {
        // フレームを生成する。
        frame = new JFrame("ななくま文具店POS端末アプリケーション");
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // ラベルを生成する。
        captionLabel = new JLabel("ななくま文具店POSシステム", SwingConstants.CENTER);
        captionLabel.setBounds(0, 0, 1024, 32);
        frame.getContentPane().add(captionLabel);

        // ベースパネルを生成する。
        basePanel = new JPanel();
        basePanel.setBounds(0, 32, 1024, 736);
        frame.getContentPane().add(basePanel);

        // ベースパネルにカードレイアウトマネージャを割り当てる。
        cardLayout = new CardLayout();
        basePanel.setLayout(cardLayout);

        // ホーム画面，商品チェック画面，会員登録画面を生成する。
        // これらの画面はカードレイアウトマネージャに管理される。
        basePanel.add(HomeScreenPanel.getInstance(), "HomeScreen");
        basePanel.add(CheckArticlesScreenPanel.getInstance(), "CheckArticlesScreen");
        basePanel.add(MemberManagementScreenPanel.getInstance(), "MemberManagementScreen");

        // 仮想カスタマディスプレイを生成し，表示をクリアする。
        // Singleton パターンにおける最初の getInstance() の呼出のため，ここで仮想カスタマディスプレイが生成されることに注意する。
        getCustomerDisplayIF().clear();

        // 仮想キャッシュドロワを生成し，ドロワを閉じる。
        // Singleton パターンにおける最初の getInstance() の呼出のため，ここで仮想キャッシュドロワが生成されることに注意する。
        getCashDrawerIF().closeDrawer();

        // 仮想プリンタを生成する。
        getPrinterIF().initialize();
    }


    /*
     *  アプリケーションを実行する。
     */

    public void run() {
        // フレームを表示する。
        frame.setVisible(true);

        // ホーム画面を選択する。
        // ホーム画面に制御が移る。
        cardLayout.show(basePanel, "HomeScreen");
    }


    /*
     *  アプリケーションの唯一のインスタンスを返す。（Singleton パターン）
     */

    public static POSTerminalApp getInstance() {
        return theApp;
    }


    /*
     *  カスタマディスプレイ I/F のインスタンスを返す。
     */

    public AbstractedCustomerDisplayIF getCustomerDisplayIF() {
        return VirtualCustomerDisplayIF.getInstance();
    }


    /*
     *  キャッシュドロワ I/F のインスタンスを返す。
     */

    public AbstractedCashDrawerIF getCashDrawerIF() {
        return VirtualCashDrawerIF.getInstance();
    }


    /*
     *  プリンタ I/F のインスタンスを返す。
     */

    public AbstractedPrinterIF getPrinterIF() {
        return VirtualPrinterIF.getInstance();
    }


    /*
     *  商品チェックが要求された場合に呼び出される。
     */

    public void checkArticlesRequested() {
        // 商品チェック画面の状態を初期化済み状態にする。
        CheckArticlesScreenPanel.getInstance().setState(CheckArticlesScreenPanelState.Initialized);

        // 商品チェック画面を選択するようカードレイアウトマネージャに委託する。
        // 商品チェック画面に制御が移る。
        cardLayout.show(basePanel, "CheckArticlesScreen");
    }


    /*
     *  会員管理が要求された場合に呼び出される。
     */

    public void memberManagementRequested() {
        // 会員管理画面の状態を何もしていない状態にする。
        MemberManagementScreenPanel.getInstance().setState(MemberManagementScreenPanelState.NoOperation);

        // 会員管理画面を選択するようカードレイアウトマネージャに委託する。
        // 会員管理画面に制御が移る。
        cardLayout.show(basePanel, "MemberManagementScreen");
    }


    /*
     *  ホーム画面に戻る。
     */

    public void returnToHomeScreen() {
        // ホーム画面を選択するようカードレイアウトマネージャに委託する。
        // ホーム画面に制御が移る。
        cardLayout.show(basePanel, "HomeScreen");
    }
}
