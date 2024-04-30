//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  会員管理画面を実現するクラス。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jp.ac.fukuoka_u.tl.NanakumaPOS.Member.Gender;


/*
 *  会員管理画面クラス
 */

public class MemberManagementScreenPanel extends JPanel implements ActionListener {
    /*
     *  会員管理画面の状態を表す列挙型
     */

    enum MemberManagementScreenPanelState {
        // 何もしていない状態
        NoOperation,
        // 会員管理業務対象の会員の情報を登録している状態
        Registering,
        // 会員管理業務対象の会員の情報を表示している状態
        Showing,
        // 会員管理業務対象の会員の情報を更新している状態
        Updating,
    }


    /*
     *  唯一のインスタンスへの参照（Singleton パターン）
     */
    private static MemberManagementScreenPanel theMemberManagementScreenPanel = null;


    /*
     *  内部データ
     */
    // 会員管理業務対象の登録会員。会員が確定していない間は null とする。
    private Member memberUnderManagement = null;

    // 会員管理画面の状態
    private MemberManagementScreenPanelState state = MemberManagementScreenPanelState.NoOperation;


    /*
     *  ウィジェット
     */
    // 会員検索画面のフレーム
    private JFrame frame = null;

    // 会員番号ラベル
    private JLabel memberIDLabel = null;

    // 会員番号欄
    private JTextField memberIDField = null;

    // 会員氏名ラベル
    private JLabel memberNameLabel = null;

    // 会員氏名欄
    private JTextField memberNameField = null;

    // フリガナラベル
    private JLabel memberFuriganaLabel = null;

    // フリガナ欄
    private JTextField memberFuriganaField = null;

    // 性別ラベル
    private JLabel memberGenderLabel = null;

    // 性別グループ
    private ButtonGroup memberGenderGroup = null;

    // 男性ラジオボタン
    private JRadioButton memberGenderMaleRadioButton = null;

    // 女性ラジオボタン
    private JRadioButton memberGenderFemaleRadioButton = null;

    // 実行ボタン
    private JButton okButton = null;

    // 中止ボタン
    private JButton cancelButton = null;

    // 会員検索ボタン
    private JButton findMemberButton = null;

    // 会員登録ボタン
    private JButton registerMemberButton = null;

    // 会員更新ボタン
    private JButton updateMemberButton = null;

    // 会員削除ボタン
    private JButton deleteMemberButton = null;

    // ホーム画面ボタン
    private JButton homeButton = null;


    /*
     *  コンストラクタ。
     *  商品チェック画面が保有するオブジェクトを生成する。
     */

    public MemberManagementScreenPanel() {
        // 内部データを初期化する。
        state = MemberManagementScreenPanelState.NoOperation;
        frame = (JFrame)SwingUtilities.getRoot(this);

        // 自身を初期化する。
        setLayout(null);

        // 会員番号入力欄を生成する。
        memberIDLabel = new JLabel("会員番号");
        memberIDLabel.setBounds(16, 16, 100, 24);
        add(memberIDLabel);
        memberIDField = new JTextField(8);
        memberIDField.setBounds(116, 16, 200, 24);
        memberIDField.setBackground(Color.YELLOW);
        add(memberIDField);

        // 会員氏名入力欄を生成する。
        memberNameLabel = new JLabel("会員氏名");
        memberNameLabel.setBounds(16, 48, 100, 24);
        add(memberNameLabel);
        memberNameField = new JTextField(8);
        memberNameField.setBounds(116, 48, 200, 24);
        add(memberNameField);

        // フリガナ入力欄を生成する。
        memberFuriganaLabel = new JLabel("フリガナ");
        memberFuriganaLabel.setBounds(16, 80, 100, 24);
        add(memberFuriganaLabel);
        memberFuriganaField = new JTextField(8);
        memberFuriganaField.setBounds(116, 80, 200, 24);
        add(memberFuriganaField);

        // 性別入力欄を生成する。
        memberGenderLabel = new JLabel("性別");
        memberGenderLabel.setBounds(16, 112, 100, 24);
        add(memberGenderLabel);
        memberGenderGroup = new ButtonGroup();
        memberGenderMaleRadioButton = new JRadioButton("男性");
        memberGenderMaleRadioButton.setBounds(116, 112, 100, 24);
        memberGenderMaleRadioButton.setSelected(true);
        add(memberGenderMaleRadioButton);
        memberGenderFemaleRadioButton = new JRadioButton("女性");
        memberGenderFemaleRadioButton.setBounds(216, 112, 100, 24);
        add(memberGenderFemaleRadioButton);
        memberGenderGroup.add(memberGenderMaleRadioButton);
        memberGenderGroup.add(memberGenderFemaleRadioButton);

        // 実行ボタンを生成する。
        okButton = new JButton("実行");
        okButton.setBounds(116, 144, 80, 24);
        okButton.addActionListener(this);
        okButton.setActionCommand("ok");
        add(okButton);

        // 中止ボタンを生成する。
        cancelButton = new JButton("中止");
        cancelButton.setBounds(216, 144, 80, 24);
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("cancel");
        add(cancelButton);

        // 会員検索ボタンを生成する。
        findMemberButton = new JButton("会員検索");
        findMemberButton.setBounds(832, 16, 160, 48);
        findMemberButton.addActionListener(this);
        findMemberButton.setActionCommand("findMember");
        add(findMemberButton);

        // 会員登録ボタンを生成する。
        registerMemberButton = new JButton("会員登録");
        registerMemberButton.setBounds(832, 80, 160, 48);
        registerMemberButton.addActionListener(this);
        registerMemberButton.setActionCommand("registerMember");
        add(registerMemberButton);

        // 会員更新ボタンを生成する。
        updateMemberButton = new JButton("会員更新");
        updateMemberButton.setBounds(832, 144, 160, 48);
        updateMemberButton.addActionListener(this);
        updateMemberButton.setActionCommand("updateMember");
        add(updateMemberButton);

        // 会員削除ボタンを生成する。
        deleteMemberButton = new JButton("会員削除");
        deleteMemberButton.setBounds(832, 208, 160, 48);
        deleteMemberButton.addActionListener(this);
        deleteMemberButton.setActionCommand("deleteMember");
        add(deleteMemberButton);

        // ホーム画面ボタンを生成する。
        homeButton = new JButton("ホーム画面");
        homeButton.setBounds(832, 272, 160, 48);
        homeButton.addActionListener(this);
        homeButton.setActionCommand("home");
        add(homeButton);

        setState(MemberManagementScreenPanelState.NoOperation);
    }


    /*
     *  会員管理画面の唯一のインスタンスを返す。（Singleton パターン）
     */

    public static MemberManagementScreenPanel getInstance() {
        if (theMemberManagementScreenPanel == null) {
            theMemberManagementScreenPanel = new MemberManagementScreenPanel();
        }
        return theMemberManagementScreenPanel;
    }


    /*
     *  会員管理画面の状態を変更する。
     */

    public void setState(MemberManagementScreenPanelState _state) {
        state = _state;
        switch (state) {
            case NoOperation:
                memberIDField.setEditable(false);
                memberNameField.setEditable(false);
                memberFuriganaField.setEditable(false);
                memberGenderMaleRadioButton.setEnabled(false);
                memberGenderFemaleRadioButton.setEnabled(false);
                okButton.setEnabled(false);
                cancelButton.setEnabled(false);
                findMemberButton.setEnabled(true);
                registerMemberButton.setEnabled(true);
                updateMemberButton.setEnabled(false);
                deleteMemberButton.setEnabled(false);
                break;
            case Registering:
                memberIDField.setEditable(true);
                memberNameField.setEditable(true);
                memberFuriganaField.setEditable(true);
                memberGenderMaleRadioButton.setEnabled(true);
                memberGenderFemaleRadioButton.setEnabled(true);
                okButton.setEnabled(true);
                cancelButton.setEnabled(true);
                findMemberButton.setEnabled(false);
                registerMemberButton.setEnabled(false);
                updateMemberButton.setEnabled(false);
                deleteMemberButton.setEnabled(false);
                break;
            case Showing:
                memberIDField.setEditable(false);
                memberNameField.setEditable(false);
                memberFuriganaField.setEditable(false);
                memberGenderMaleRadioButton.setEnabled(false);
                memberGenderFemaleRadioButton.setEnabled(false);
                okButton.setEnabled(false);
                cancelButton.setEnabled(false);
                findMemberButton.setEnabled(true);
                registerMemberButton.setEnabled(true);
                updateMemberButton.setEnabled(true);
                deleteMemberButton.setEnabled(true);
                break;
            case Updating:
                memberIDField.setEditable(false);
                memberNameField.setEditable(true);
                memberFuriganaField.setEditable(true);
                memberGenderMaleRadioButton.setEnabled(true);
                memberGenderFemaleRadioButton.setEnabled(true);
                okButton.setEnabled(true);
                cancelButton.setEnabled(true);
                findMemberButton.setEnabled(false);
                registerMemberButton.setEnabled(false);
                updateMemberButton.setEnabled(false);
                deleteMemberButton.setEnabled(false);
                break;
        }
    }


    /*
     *  会員情報の妥当性を検証する。
     */

    private boolean validateMemberInfo() {
        if (memberIDField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "会員番号が空欄です。");
            memberIDField.requestFocusInWindow();
            return false;
        }
        if (memberNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "会員氏名が空欄です。");
            memberNameField.requestFocusInWindow();
            return false;
        }
        if (memberFuriganaField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "フリガナが空欄です。");
            memberFuriganaField.requestFocusInWindow();
            return false;
        }
        return true;
    }


    /*
     *  会員検索が要求されたときに呼び出される。
     */

    private void memberFindingRequested() {
        // 店員に会員番号の入力を求める。
        String memberID = JOptionPane.showInputDialog(frame, "会員番号を入力してください。");
        if (memberID != null) {
            // 入力があった場合，会員検索を行う。
            try {
                memberUnderManagement = DBServerIF.getInstance().findMember(memberID);
                memberUnderManagementChanged();
                setState(MemberManagementScreenPanelState.Showing);
            }
            catch (DBServerIF.DBServerIFException ex) {
                // データベースのアクセスに問題がある場合，問題の発生を店員に知らせる。
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /*
     *  会員登録が要求されたときに呼び出される。
     */

    private void memberRegistrationRequested() {
        // 会員が誰も選択されていない状態にする。
        memberUnderManagement = null;
        memberUnderManagementChanged();
        // 新しい会員登録に向けて画面を設定する。
        setState(MemberManagementScreenPanelState.Registering);
        memberIDField.requestFocusInWindow();
    }


    /*
     *  会員登録が確定されたときに呼び出さされる。
     */

    private void memberRegistrationConfirmed() {
        Member member = new Member("", "", "", Gender.Male);

        if (JOptionPane.showConfirmDialog(frame,  "会員登録しますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            if (!validateMemberInfo()) {
                return;
            }
            //@@@ データベースに会員登録を依頼する部分は未実装。
            //todo
            member.setID(memberIDField.getText());
            member.setName(memberNameField.getText());
            member.setFurigana(memberFuriganaField.getText());
            Gender gender = memberGenderMaleRadioButton.isSelected() ? Gender.Male: Gender.Female;
            member.setGender(gender);
            try{
                DBServerIF.getInstance().registerMember(member);
                memberUnderManagement = member;
                memberUnderManagementChanged();
                setState(MemberManagementScreenPanelState.Showing);
            }catch (DBServerIF.DBServerIFException ex){
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /*
     *  会員登録が中止されたときに呼び出される。
     */

    private void memberRegistrationCancelled() {
        if (JOptionPane.showConfirmDialog(frame, "会員登録を中止しますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            setState(MemberManagementScreenPanelState.NoOperation);
        }
    }


    /*
     *  会員更新が要求されたときに呼び出される。
     */

    private void memberUpdatingRequested() {
        setState(MemberManagementScreenPanelState.Updating);
        memberNameField.requestFocusInWindow();
    }


    /*
     *  会員更新が確定されたときに呼び出される。
     */

    private void memberUpdatingConfirmed() {
        if (JOptionPane.showConfirmDialog(frame,  "会員情報を更新しますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            if (!validateMemberInfo()) {
                return;
            }
            //@@@ データベースに会員の更新を依頼する部分は未実装。
            //todo
            memberUnderManagement.setID(memberIDField.getText());
            memberUnderManagement.setName(memberNameField.getText());
            memberUnderManagement.setFurigana(memberFuriganaField.getText());
            Gender gender = memberGenderMaleRadioButton.isSelected() ? Gender.Male: Gender.Female;
            memberUnderManagement.setGender(gender);
            try {
                DBServerIF.getInstance().updateMember(memberUnderManagement);
                //memberUnderManagement = member;
                memberUnderManagementChanged();
                setState(MemberManagementScreenPanelState.Showing);
            }
            catch (DBServerIF.DBServerIFException ex){
                JOptionPane.showMessageDialog(frame, ex.getMessage(),"エラー",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /*
     *  会員更新が中止されたときに呼び出される。
     */

    private void memberUpdatingCancelled() {
        if (JOptionPane.showConfirmDialog(frame, "会員情報の更新を中止しますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
        }
    }


    /*
     *  会員削除が要求されたときに呼び出される。
     */

    private void memberDeletionRequested() {
        String memberName = memberUnderManagement.getName();
        String memberID = memberUnderManagement.getID();
        if (JOptionPane.showConfirmDialog(frame, "会員「" + memberName + "」を削除しますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            //@@@ データベースに会員の削除を依頼する部分は未実装。
            //todo
            try {
                DBServerIF.getInstance().deleteMember(memberID);
                memberUnderManagementChanged();
                setState(MemberManagementScreenPanelState.NoOperation);
            }
            catch (DBServerIF.DBServerIFException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            }


            // 会員が誰も選択されていない状態にする。
            memberUnderManagement = null;
            memberUnderManagementChanged();
        }
    }


    /*
     *  会員管理のキャンセルが要求されたときに呼び出される。
     */

    private void memberManagementCancelled() {
        boolean do_cancel = false;

        switch (state) {
            case Registering:
            case Updating:
                // 会員情報の登録中または更新中にキャンセルするとそれまでの入力が無駄になるので，店員に確認が必要である。
                if (JOptionPane.showConfirmDialog(frame, "会員管理をキャンセルしますか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    do_cancel = true;
                }
                break;
            case NoOperation:
            case Showing:
                do_cancel = true;
                break;
        }
        // 会員管理をキャンセルする。
        if (do_cancel) {
            //
            state = MemberManagementScreenPanelState.NoOperation;
            // 会員が誰も選択されていない状態にする。
            memberUnderManagement = null;
            memberUnderManagementChanged();
            // 会員管理をキャンセルする。
            POSTerminalApp.getInstance().returnToHomeScreen();
        }
    }


    /*
     * 会員検索画面上のボタンが押されるときに呼び出される。
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd)
        {
            case "ok":
                switch (state) {
                    case Registering:
                        memberRegistrationConfirmed();
                        break;
                    case Updating:
                        memberUpdatingConfirmed();
                        break;
                    case NoOperation:
                    case Showing:
                        // これらの状態では実行ボタンを押せなくしているので，ここが実行されることはない。
                        break;
                }
                break;
            case "cancel":
                switch (state) {
                    case Registering:
                        memberRegistrationCancelled();
                        break;
                    case Updating:
                        memberUpdatingCancelled();
                        break;
                    case NoOperation:
                    case Showing:
                        // これらの状態では中止ボタンを押せなくしているので，ここが実行されることはない。
                        break;
                }
                break;
            case "findMember":
                // 会員検索ボタンが押されたときは会員検索処理を呼び出す。
                memberFindingRequested();
                break;
            case "registerMember":
                // 会員登録ボタンが押されたときは会員登録処理を呼び出す。
                memberRegistrationRequested();
                break;
            case "updateMember":
                // 会員更新ボタンが押されたときは会員更新処理を呼び出す。
                memberUpdatingRequested();
                break;
            case "deleteMember":
                // 会員削除ボタンが押されたときは会員削除処理を呼び出す。
                memberDeletionRequested();
                break;
            case "home":
                // ホーム画面ボタンが押下されたときは会員管理キャンセル処理を呼び出す。
                memberManagementCancelled();
                break;
            default:
                //@@@ エラー
                ;
        }
    }


    /*
     *  管理対象の会員が更新されたら呼び出される。
     */

    public void memberUnderManagementChanged() {
        if (memberUnderManagement != null) {
            memberIDField.setText(memberUnderManagement.getID());
            memberNameField.setText(memberUnderManagement.getName());
            memberFuriganaField.setText(memberUnderManagement.getFurigana());
            memberGenderMaleRadioButton.setSelected(memberUnderManagement.getGender() == Gender.Male);
            memberGenderFemaleRadioButton.setSelected(memberUnderManagement.getGender() == Gender.Female);
        } else {
            memberIDField.setText("");
            memberNameField.setText("");
            memberFuriganaField.setText("");
            memberGenderMaleRadioButton.setSelected(false);
            memberGenderFemaleRadioButton.setSelected(false);
        }
    }
}
