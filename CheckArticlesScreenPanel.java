//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  商品チェック画面を実現するクラス。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.sql.*;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import jp.ac.fukuoka_u.tl.NanakumaPOS.DBServerIF.DBServerIFException;


/*
 *  商品チェック画面クラス
 */

public class CheckArticlesScreenPanel extends JPanel implements ActionListener, TableModelListener {
    private CallableStatement pstmt;
    private String sql;
    /*
     *  商品チェック画面の状態を表す列挙型
     */

    public enum CheckArticlesScreenPanelState {
        // 未初期化
        NotInitialized,
        // 初期化済
        Initialized,
        // 商品チェック中
        CheckingArticles,
        // 決済済
        PaymentFinished,
    }


    /*
     *  商品チェック画面クラスの唯一のインスタンス（Singleton パターン）
     */

    private static CheckArticlesScreenPanel checkArticlesScreenPanel = null;


    /*
     *  内部データ
     */
    // 商品チェック中の登録会員。会員が確定していない間は null とする。
    private Member memberUnderChecking = null;

    // 商品チェック中の決済対象商品販売のリスト
    private SalesList salesUnderChecking = null;

    // フォーカスされている商品販売の番号
    private int salesIndexInFocus = -1;

    // 商品チェック画面の状態
    private CheckArticlesScreenPanelState state = CheckArticlesScreenPanelState.NotInitialized;


    /*
     *  ウィジェット
     */
    // 商品チェック画面のフレーム
    private JFrame frame = null;

    // スクロールペイン
    private JScrollPane scroll = null;

    // 販売商品表
    private JTable articlesTable = null;

    // 会員番号入力ボタン
    private JButton enterMembershipIDButton = null;

    // 販売単価変更ボタン
    private JButton changeSalesPriceButton = null;

    // 販売個数変更ボタン
    private JButton changeQuantityButton = null;

    // 決済ボタン
    private JButton paymentButton = null;

    // ホームボタン
    private JButton homeButton = null;

    // 商品コードラベル
    private JLabel articleCodeLabel = null;

    // 商品コード入力欄
    private JTextField articleCodeField = null;

    // 会員番号ラベル
    private JLabel memberIDLabel = null;

    // 会員番号欄
    private JTextField memberIDField = null;

    // 会員氏名ラベル
    private JLabel memberNameLabel = null;

    // 会員氏名欄
    private JTextField memberNameField = null;

    // 保有ポイントラベル
    private JLabel memberPointLabel = null;

    // 保有ポイント欄
    private JTextField memberPointField = null;

    // 商品コード入力ボタン
    private JButton articleCodeButton = null;

    // 合計金額ラベル
    private JLabel totalPriceLabel = null;

    // 合計金額欄
    private JTextField totalPriceField = null;

    // お預かりラベル
    private JLabel paidPriceLabel = null;

    // お預かり金額欄
    private JTextField paidPriceField = null;

    // おつりラベル
    private JLabel changePriceLabel = null;

    // おつり金額欄
    private JTextField changePriceField = null;


    /*
     *  コンストラクタ。
     *  商品チェック画面が保有するオブジェクトを生成する。
     */

    private CheckArticlesScreenPanel() {
        // 内部データを初期化する。
        frame = (JFrame)SwingUtilities.getRoot(this);
        state = CheckArticlesScreenPanelState.Initialized;
        salesIndexInFocus = -1;
        salesUnderChecking = new SalesList();

        // 自身を初期化する。
        setLayout(null);

        // 販売商品一覧画面を生成する。
        DefaultTableCellRenderer rightAlignmentRenderer = new DefaultTableCellRenderer();
        rightAlignmentRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        articlesTable = new JTable(salesUnderChecking);
        salesUnderChecking.addTableModelListener(this);
        articlesTable.setEnabled(false);
        articlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articlesTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        articlesTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        articlesTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        articlesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        articlesTable.getColumnModel().getColumn(3).setCellRenderer(rightAlignmentRenderer);
        articlesTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        articlesTable.getColumnModel().getColumn(4).setCellRenderer(rightAlignmentRenderer);
        articlesTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        articlesTable.getColumnModel().getColumn(5).setCellRenderer(rightAlignmentRenderer);
        scroll = new JScrollPane(articlesTable);
        scroll.setBounds(16, 16, 800, 524);
        add(scroll);

        // 会員番号入力ボタンを生成する。
        enterMembershipIDButton = new JButton("会員番号入力");
        enterMembershipIDButton.setBounds(832, 16, 160, 48);
        enterMembershipIDButton.addActionListener(this);
        enterMembershipIDButton.setActionCommand("enterMembershipID");
        add(enterMembershipIDButton);

        // 販売単価変更ボタンを生成する。
        changeSalesPriceButton = new JButton("販売単価変更");
        changeSalesPriceButton.setBounds(832, 80, 160, 48);
        changeSalesPriceButton.addActionListener(this);
        changeSalesPriceButton.setActionCommand("changeSalesPrice");
        add(changeSalesPriceButton);

        // 販売個数変更ボタンを生成する。
        changeQuantityButton = new JButton("販売個数変更");
        changeQuantityButton.setBounds(832, 144, 160, 48);
        changeQuantityButton.addActionListener(this);
        changeQuantityButton.setActionCommand("changeQuantity");
        add(changeQuantityButton);

        // 決済ボタンを生成する。
        paymentButton = new JButton("決済");
        paymentButton.setBounds(832, 208, 160, 48);
        paymentButton.addActionListener(this);
        paymentButton.setActionCommand("payment");
        add(paymentButton);

        // ホームボタンを生成する。
        homeButton = new JButton("ホーム画面");
        homeButton.setBounds(832, 272, 160, 48);
        homeButton.addActionListener(this);
        homeButton.setActionCommand("home");
        add(homeButton);

        // 商品コード入力欄を生成する。
        articleCodeLabel = new JLabel("商品コード入力");
        articleCodeLabel.setBounds(16, 568, 100, 24);
        add(articleCodeLabel);
        articleCodeField = new JTextField(8);
        articleCodeField.setBounds(116, 568, 100, 24);
        articleCodeField.setBackground(Color.YELLOW);
        articleCodeField.setHorizontalAlignment(JTextField.LEFT);
        articleCodeField.setEditable(true);
        add(articleCodeField);

        // 商品コード入力ボタンを生成する。
        articleCodeButton = new JButton("入力");
        articleCodeButton.setBounds(226, 568, 80, 24);
        articleCodeButton.addActionListener(this);
        articleCodeButton.setActionCommand("articleCode");
        add(articleCodeButton);

        // 会員番号欄を生成する。
        memberIDLabel = new JLabel("会員番号");
        memberIDLabel.setBounds(396, 568, 100, 24);
        add(memberIDLabel);
        memberIDField = new JTextField(8);
        memberIDField.setBounds(496, 568, 100, 24);
        memberIDField.setBackground(Color.CYAN);
        memberIDField.setEditable(false);
        add(memberIDField);

        // 会員氏名欄を生成する。
        memberNameLabel = new JLabel("会員氏名");
        memberNameLabel.setBounds(396, 600, 100, 24);
        add(memberNameLabel);
        memberNameField = new JTextField(32);
        memberNameField.setBounds(496, 600, 100, 24);
        memberNameField.setBackground(Color.CYAN);
        memberNameField.setEditable(false);
        add(memberNameField);

        // 保有ポイント欄を生成する。
        memberPointLabel = new JLabel("保有ポイント");
        memberPointLabel.setBounds(396, 632, 100, 24);
        add(memberPointLabel);
        memberPointField = new JTextField(32);
        memberPointField.setBounds(496, 632, 100, 24);
        memberPointField.setBackground(Color.CYAN);
        memberPointField.setEditable(false);
        add(memberPointField);

        // お預かり欄を生成する。
        paidPriceLabel = new JLabel("お預かり");
        paidPriceLabel.setBounds(616, 568, 100, 24);
        add(paidPriceLabel);
        paidPriceField = new JTextField(8);
        paidPriceField.setBounds(716, 568, 100, 24);
        paidPriceField.setBackground(Color.CYAN);
        paidPriceField.setHorizontalAlignment(JTextField.RIGHT);
        paidPriceField.setEditable(false);
        add(paidPriceField);

        // 合計金額欄を生成する。
        totalPriceLabel = new JLabel("合計金額");
        totalPriceLabel.setBounds(616, 600, 100, 24);
        add(totalPriceLabel);
        totalPriceField = new JTextField(8);
        totalPriceField.setBounds(716, 600, 100, 24);
        totalPriceField.setBackground(Color.CYAN);
        totalPriceField.setHorizontalAlignment(JTextField.RIGHT);
        totalPriceField.setEditable(false);
        add(totalPriceField);

        // おつり欄を生成する。
        changePriceLabel = new JLabel("おつり");
        changePriceLabel.setBounds(616, 632, 100, 24);
        add(changePriceLabel);
        changePriceField = new JTextField(8);
        changePriceField.setBounds(716, 632, 100, 24);
        changePriceField.setBackground(Color.CYAN);
        changePriceField.setHorizontalAlignment(JTextField.RIGHT);
        changePriceField.setEditable(false);
        add(changePriceField);

        // 内部データとウィジェットを初期化する。
        setState(CheckArticlesScreenPanelState.Initialized);
    }


    /*
     *  商品チェック画面の唯一のインスタンスへの参照を返す。
     */

    public static CheckArticlesScreenPanel getInstance() {
        if (checkArticlesScreenPanel == null) {
            checkArticlesScreenPanel = new CheckArticlesScreenPanel();
        }
        return checkArticlesScreenPanel;
    }


    /*
     *  商品チェック画面の状態を変更する。
     */

    public void setState(CheckArticlesScreenPanelState _state) {
        state = _state;
        switch (state) {
            case Initialized:
                // 商品チェック中のユーザが選択されていない状態にする。
                memberUnderChecking = null;
                // 決済対象商品販売リストをクリアする。
                salesUnderChecking.clear();
                salesUnderChecking.fireTableDataChanged();
                // ウィジェットを初期化する。
                articleCodeField.setText("");
                articleCodeField.setEnabled(true);
                articleCodeButton.setEnabled(true);
                memberIDField.setText("");
                memberNameField.setText("");
                memberPointField.setText("");
                paidPriceField.setText("");
                totalPriceField.setText("0");
                changePriceField.setText("");
                enterMembershipIDButton.setEnabled(true);
                changeSalesPriceButton.setEnabled(false);
                changeQuantityButton.setEnabled(false);
                paymentButton.setEnabled(false);
                articleCodeField.requestFocusInWindow();
                break;
            case CheckingArticles:
                articleCodeButton.setEnabled(true);
                enterMembershipIDButton.setEnabled(true);
                changeSalesPriceButton.setEnabled(false);
                changeQuantityButton.setEnabled(false);
                paymentButton.setEnabled(true);
                break;
            case PaymentFinished:
                articlesTable.clearSelection();
                articleCodeField.setEnabled(false);
                articleCodeButton.setEnabled(false);
                enterMembershipIDButton.setEnabled(false);
                changeSalesPriceButton.setEnabled(false);
                changeQuantityButton.setEnabled(false);
                paymentButton.setEnabled(false);
                break;
        }
    }


    /*
     *  商品コード入力欄をクリアする。
     */

    public void clearArticleCodeField() {
        articleCodeField.setText("");
        articleCodeField.requestFocusInWindow();
    }


    /*
     *  お預かり欄に金額を表示する。
     */

    public void setPaidPrice(int paidPrice) {
        paidPriceField.setText(Integer.toString(paidPrice));
    }


    /*
     *  おつり欄に金額を表示する。
     */

    public void setChangePrice(int changePrice) {
        changePriceField.setText(Integer.toString(changePrice));
    }


    /*
     *  商品販売表上の idx 番目の商品販売にフォーカスをあてる。
     */

    public void focusSales(int idx) {
        // 商品チェック画面が商品チェック中状態の場合のみ処理する。
        if (state == CheckArticlesScreenPanelState.CheckingArticles) {
            // 商品販売表上の当該商品販売にフォーカスをあてる。
            salesIndexInFocus = idx;
            articlesTable.setRowSelectionInterval(idx, idx);
            // 当該商品販売の販売単価と販売個数を変更できるように販売単価変更ボ
            // タンと販売個数変更ボタンを有効にする。
            changeSalesPriceButton.setEnabled(true);
            changeQuantityButton.setEnabled(true);
        }
    }


    /*
     *  商品販売表上の商品販売のフォーカスをはずす。
     */

    public void unfocusSales() {
        // 商品販売表上のすべての商品販売のフォーカスをはずす。
        salesIndexInFocus = -1;
        articlesTable.clearSelection();
        // 販売単価変更ボタンと販売個数変更ボタンを無効にする。
        changeSalesPriceButton.setEnabled(false);
        changeQuantityButton.setEnabled(false);
    }


    /*
     *  商品コードが入力されたときに呼び出される。
     */

    private void articleCodeEntered() {
        String articleCode = articleCodeField.getText();
        int idx;
        Article article;
        Sale sale;

        // 決済対象商品販売のリストに当該コードの商品の販売が含まれていないか検査する。
        idx = salesUnderChecking.find(articleCode);
        // 含まれている場合，
        if (idx >= 0) {
            // 商品販売表中当該商品販売にフォーカスをあてる。
            focusSales(idx);
            // チェック済みの商品であることを店員に知らせる。
            JOptionPane.showMessageDialog(frame,  "すでにチェックされている商品です。販売個数を確認してください。", "注意", JOptionPane.WARNING_MESSAGE);
            // 商品チェック画面の商品コード入力欄をクリアしフォーカスをあてる。
            clearArticleCodeField();
            return;
        }

        // 含まれていない場合，
        try {
            // 商品の名称と定価をデータベースから検索する。
            article = DBServerIF.getInstance().findArticle(articleCode);
            // 当該商品販売を決済対象商品販売リストに追加する。
            sale = new Sale(article.getArticleCode(), article.getArticleName(), article.getCataloguePrice(), 1);
            salesUnderChecking.add(sale);
            idx = salesUnderChecking.getNumOfSales() - 1;
            salesUnderChecking.fireTableRowsInserted(idx, idx);
            // 商品チェック画面を商品チェック中状態にする。
            setState(CheckArticlesScreenPanelState.CheckingArticles);
            // 商品販売表中の当該商品販売にフォーカスをあてる。
            focusSales(idx);
            // カスタマディスプレイに当該商品販売を表示する。
            displaySalesOnCustomerDisplay(sale);
            // 商品チェック画面の商品コード入力欄をクリアしフォーカスをあてる。
            clearArticleCodeField();
        }
        catch (DBServerIFException ex) {
            // データベースそのものに問題がある場合，
            // またはデータベースのアクセスに問題がある場合，
            // 商品販売表中のすべての商品販売についてフォーカスをはずす。
            unfocusSales();
            // 問題の発生を店員に知らせる。
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }
        articleCodeField.requestFocusInWindow();
    }


    /*
     *  販売単価の変更が要求されたときに呼び出される。
     */

    private void changeSalesPriceRequested() {
        // フォーカスされている商品販売の販売単価を取得する。
        int salesPrice = salesUnderChecking.getIthSale(salesIndexInFocus).getSalesPrice();

        // 店員に販売単価の入力を求める。
        String str = JOptionPane.showInputDialog(frame, "販売単価を入力してください。", salesPrice);
        if (str != null) {
            // 入力があった場合，
            try {
                // 入力内容（文字列）を販売単価（整数）に変換する。
                salesPrice = Integer.parseInt(str);
                // 販売単価が0未満の場合は例外を投げる。
                if (salesPrice < 0) {
                    throw new NumberFormatException();
                }
                // 決済対象商品販売リストを更新する。
                salesUnderChecking.getIthSale(salesIndexInFocus).setSalesPrice(salesPrice);
                salesUnderChecking.fireTableRowsUpdated(salesIndexInFocus, salesIndexInFocus);
                // カスタマディスプレイに表示する。
                displaySalesOnCustomerDisplay(salesUnderChecking.getIthSale(salesIndexInFocus));
            }
            // 入力はあってもその書式が不正の場合，
            catch (NumberFormatException ex) {
                // 店員にエラーを通知する。
                JOptionPane.showMessageDialog(frame, "販売単価の入力が不正です。", "エラー", JOptionPane.ERROR_MESSAGE);
            }
        }
        // 商品コード入力欄にフォーカスをあてる。
        articleCodeField.requestFocusInWindow();
    }


    /*
     *  会員番号の入力が要求されたときに呼び出される。
     */

    private void memberIDEnteringRequested() {
        // 店員に会員番号の入力を求める。
        String memberID = JOptionPane.showInputDialog(frame, "会員番号を入力してください。");
        try {
            memberUnderChecking = DBServerIF.getInstance().findMember(memberID);
            if (memberUnderChecking != null) {
                memberIDField.setText(memberUnderChecking.getID());
                memberNameField.setText(memberUnderChecking.getName());
                memberPointField.setText(Integer.toString(memberUnderChecking.getPoint()));
            }
        }
        catch (DBServerIFException ex) {
            // 問題の発生を店員に知らせる。
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
        }
        articleCodeField.requestFocusInWindow();
    }


    /*
     *  商品販売 sale に関する情報をカスタマディスプレイに表示する。
     */

    private void displaySalesOnCustomerDisplay(Sale sale) {
        AbstractedCustomerDisplayIF customerDisplayIF = POSTerminalApp.getInstance().getCustomerDisplayIF();
        customerDisplayIF.displayUpperMessage(sale.getArticleName(), AbstractedCustomerDisplayIF.Alignment.LEFT);
        String buf = "@" + Integer.toString(sale.getSalesPrice()) + "x" + Integer.toString(sale.getSalesQuantity());
        customerDisplayIF.displayLowerMessage(buf, AbstractedCustomerDisplayIF.Alignment.RIGHT);
    }


    /*
     *  販売個数の変更が要求されたときに呼び出される。
     */

    private void changeSalesQuantityRequested() {
        // フォーカスされている商品販売の販売個数を取得する。
        int salesQuantity = salesUnderChecking.getIthSale(salesIndexInFocus).getSalesQuantity();

        // 店員に販売個数の入力を求める。
        String str = JOptionPane.showInputDialog(frame, "販売個数を入力してください。", salesQuantity);
        if (str != null) {
            // 入力があった場合，
            try {
                // 入力内容（文字列）を販売個数（整数）に変換する。
                salesQuantity = Integer.parseInt(str);
                // 販売個数が0未満の場合は例外を投げる。
                if (salesQuantity < 0) {
                    throw new NumberFormatException();
                }
                // 販売個数の変更を確定する。
                salesUnderChecking.getIthSale(salesIndexInFocus).setSalesQuantity(salesQuantity);
                salesUnderChecking.fireTableRowsUpdated(salesIndexInFocus, salesQuantity);
                displaySalesOnCustomerDisplay(salesUnderChecking.getIthSale(salesIndexInFocus));
            }
            // 入力はあってもその書式が不正の場合，
            catch (NumberFormatException ex) {
                // 店員にエラーを通知する。
                JOptionPane.showMessageDialog(frame, "販売個数の入力が不正です。", "エラー", JOptionPane.ERROR_MESSAGE);
            }
        }
        // 商品コード入力欄にフォーカスをあてる。
        articleCodeField.requestFocusInWindow();
    }


    /*
     *  決済が要求された場合に呼び出される。
     */

    public boolean paymentRequested() throws DBServerIFException {
        AbstractedCustomerDisplayIF customerDisplayIF = POSTerminalApp.getInstance().getCustomerDisplayIF();
        AbstractedCashDrawerIF cashDrawerIF = POSTerminalApp.getInstance().getCashDrawerIF();
        AbstractedPrinterIF printerIF = POSTerminalApp.getInstance().getPrinterIF();


        // 決済対象商品販売の合計金額を得る。
        int totalPrice = salesUnderChecking.getTotalPrice();
        int totalpayment = totalPrice;

        // カスタマディスプレイに合計金額を表示する。
        customerDisplayIF.displayUpperMessage("合計金額", AbstractedCustomerDisplayIF.Alignment.LEFT);
        customerDisplayIF.displayLowerMessage(Integer.toString(totalPrice), AbstractedCustomerDisplayIF.Alignment.RIGHT);

        // お預かりの入力を求める。
        int totalpoint = memberUnderChecking != null ?  memberUnderChecking.getPoint() : 0;
        PaymentDialog paymentDialog = new PaymentDialog(frame, totalPrice,totalpoint);
        paymentDialog.setVisible(true);

        // お預かりの入力がキャンセルされた場合は決済もキャンセルする。
        if (!paymentDialog.isConfirmed()) {
            articleCodeField.requestFocusInWindow();
            return false;
        }
        //使用ポイントを得る
        int paidPoint = paymentDialog.getPaidPoint();

        //支払い額から使用ポイントを差し引く
        totalPrice -= paidPoint;
        //保有ポイント減算
        totalpoint -= paidPoint;

        int addpoint = (int)Math.floor(totalPrice*0.01);

        if(memberUnderChecking != null) {
            DBServerIF.getInstance().decreasePointAmount(memberUnderChecking.getID(), paidPoint);
            //保有ポイント加算
            DBServerIF.getInstance().addPointAmount(memberUnderChecking.getID(), addpoint);
        }

        // お預かり額を得る。
        int paidPrice = paymentDialog.getPaidPrice();


        // おつりを計算する。
        int changePrice = paidPrice - totalPrice;

        // お預かり額とおつりを商品チェック画面に表示する。
        setPaidPrice(paidPrice);
        setChangePrice(changePrice);

        // カスタマディスプレイにおつりを表示する。
        customerDisplayIF.displayUpperMessage("おつり", AbstractedCustomerDisplayIF.Alignment.LEFT);
        customerDisplayIF.displayLowerMessage(Integer.toString(changePrice), AbstractedCustomerDisplayIF.Alignment.RIGHT);

        // レシートを印刷する。
        //@@@ 未実装
        //当該決済対象商品販売リスト内の商品販売の件数を返す。
        int ith=salesUnderChecking.getNumOfSales();
        //商品名
        String ArticleName[] = new String[ith];
        //販売単価
        int salesPrice[] = new int[ith];
        //販売個数
        int salesQuantity[] = new int[ith];
        //ポイント残高
        int pointbalance;
        //ポイント加減算額
        int pointaddloss;

        int i;

        printerIF.initialize();

        for(i=0;i<ith;i++){
            //商品販売の商品名を取得する。
            ArticleName[i] = salesUnderChecking.getIthSale(i).getArticleName();
            //商品販売の販売単価を取得する。
            salesPrice[i] = salesUnderChecking.getIthSale(i).getSalesPrice();
            //商品販売の販売個数を取得する。
            salesQuantity[i] = salesUnderChecking.getIthSale(i).getSalesQuantity();
            //商品名を印刷する。
            printerIF.print("商品名:");
            printerIF.print(ArticleName[i]);
            printerIF.print(" ");
            //販売単価を印刷する。
            printerIF.print("販売単価:");
            printerIF.print(salesPrice[i]);
            printerIF.print(" ");
            //販売個数を印刷する。
            printerIF.print("販売個数:");
            printerIF.print(salesQuantity[i]);
            printerIF.print(" \n");
        }
        //合計金額を印刷する。
        printerIF.print("合計金額:");
        printerIF.print(totalpayment);
        printerIF.print(" ");
        //お預かり額を印刷する。
        printerIF.print("お預かり額:");
        printerIF.print(paidPrice);
        printerIF.print(" ");
        //おつりを印刷する。
        printerIF.print("おつり:");
        printerIF.print(changePrice);
        printerIF.print(" \n");


        if(memberUnderChecking != null){
            //ポイント残高、ポイント加減算額の情報を得る。
            pointbalance = DBServerIF.getInstance().getCurrentPoint(memberUnderChecking.getID());
            pointaddloss = addpoint-paidPoint;

            //レシートにポイント残高、ポイント加減算額の情報を印刷する。
            printerIF.print("ポイント残高:");
            printerIF.print(pointbalance);
            printerIF.print(" ");

            printerIF.print("ポイント加減算額:");
            printerIF.print(pointaddloss);
            printerIF.print(" \n");
        }
        //todo
        //aaaaa


        // キャッシュドロワを開ける。
        cashDrawerIF.openDrawer();

        // データベースを更新する。
        //@@@ 未実装
        //todo


        ZonedDateTime zonedDateTime;
        Timestamp salesdate[] = new Timestamp[ith];
        String salescode[] = new String[ith];

        for(int k=0;k<ith;k++){
            zonedDateTime = salesUnderChecking.getIthSale(k).getSalesDate();
            salesdate[k] = Timestamp.from(zonedDateTime.toInstant()); //ZonedDateTime型をTimestamp型に変換
            salescode[k] = salesUnderChecking.getIthSale(k).getArticleCode();
        }
        ///データベース接続の確立
        String url = "jdbc:mysql://vhost.cx.tl.fukuoka-u.ac.jp:13306/nanakumapos3";
        String user = "root";
        String password = "B3PBL%mysql";
        for(int k=0;k<ith;k++) {

            ///SQLステートメントの実行
//           String sql = "INSERT INTO salestbl (purchased_at, article_code, article_name, price, amount, member_id) VALUES (?, ?, ?, ?, ?, ?)";

            String sql = "INSERT INTO salestbl (purchased_at, article_code, article_name, price, amount, member_id) VALUES (?, ?, ?, ?, ?, ?)";/*.formatted(salesdate[k], salescode[k],ArticleName[k],salesPrice[k],salesQuantity[k]);*/


            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);)
            {

                    pstmt.setTimestamp(1, salesdate[k]);
                    pstmt.setString(2, salescode[k]);
                    pstmt.setString(3, ArticleName[k]);
                    pstmt.setInt(4, salesPrice[k]);
                    pstmt.setInt(5, salesQuantity[k]);
                if(memberUnderChecking == null ){
                    pstmt.setNull(6,java.sql.Types.CHAR);
                }else{
                    pstmt.setString(6, memberUnderChecking.getID());
                }

                //// クエリの実行
                //PreparedStatement pstmt = conn.prepareStatement(sql)
                int affectedRows = pstmt.executeUpdate();

                //// 挿入された行数を出力（任意）
                System.out.println(affectedRows + " rows inserted.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




        // 商品チェック画面を決済済み状態にする。
        checkArticlesScreenPanel.setState(CheckArticlesScreenPanelState.PaymentFinished);
        homeButton.requestFocusInWindow();
        return true;
    }


    /*
     *  商品チェックのキャンセルが要求されたときに呼び出される。
     */

    private void checkArticlesCancelled() {
        AbstractedCustomerDisplayIF customerDisplayIF = POSTerminalApp.getInstance().getCustomerDisplayIF();
        switch (state) {
            case Initialized:
            case PaymentFinished:
                // 商品チェック画面が初期化済み状態の場合，商品は何もチェックされて
                // いない状態なので問答無用でキャンセルして構わない。
                // 商品チェック画面が決済済みの場合，商品はすべてチェックされて決済
                // が完了している状態なので問答無用でキャンセルして構わない。
                // カスタマディスプレイの表示をクリアする。
                customerDisplayIF.clear();
                POSTerminalApp.getInstance().returnToHomeScreen();
                break;
            case CheckingArticles:
                // 商品チェック画面が商品チェック中状態の場合，キャンセルするとそれ
                // までの入力が無駄になるので，店員に確認が必要である。
                if (JOptionPane.showConfirmDialog(frame, "商品のチェックをキャンセルしますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    // キャンセル応諾の場合，商品チェックをキャンセルする。
                    customerDisplayIF.clear();
                    POSTerminalApp.getInstance().returnToHomeScreen();
                } else {
                    // キャンセル拒否の場合，商品コード入力欄にフォーカスをあてる。
                    articleCodeField.requestFocusInWindow();
                }
                break;
        }
    }


    /*
     *  商品チェック画面上のボタンが押されるときに呼び出される。
     */

    @Override
    public void actionPerformed (ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd)
        {
            case "articleCode":
                // 商品コード入力ボタンが押下されたときは商品コード入力処理を呼び出す。
                articleCodeEntered();
                break;
            case "enterMembershipID":
                // 会員番号入力ボタンが押下されたときは会員番号入力処理を呼び出す。
                memberIDEnteringRequested();
                break;
            case "changeSalesPrice":
                // 商品単価変更ボタンが押下されたときは商品単価変更処理を呼び出す。
                changeSalesPriceRequested();
                break;
            case "changeQuantity":
                // 商品個数変更ボタンが押下されたときは商品個数変更処理を呼び出す。
                changeSalesQuantityRequested();
                break;
            case "payment":
                // 決済ボタンが押下されたときは決済処理を呼び出す。
                try {
                    paymentRequested();
                } catch (DBServerIFException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "home":
                // ホーム画面ボタンが押下されたときは商品チェックキャンセル処理を呼び出す。
                checkArticlesCancelled();
                break;
        }
    }


    /*
     *  決済対象商品販売リストが更新されたら呼び出される。
     */

    @Override
    public void tableChanged(TableModelEvent e) {
        // 合計金額欄の表示を更新する。
        totalPriceField.setText(Integer.toString(salesUnderChecking.getTotalPrice()));
    }
}
