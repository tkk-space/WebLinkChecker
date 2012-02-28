package kt0319;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.Font;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import javax.swing.text.Document;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public JTextField textField;
	public JTextField textField_1;
	public JTextField URL_textfield;
	public JTextField NG_textField;
	public JTextField PIC_textField;
	public JTextArea csv_text;
	public JTextField limit_level_textField;
	public JTextField limit_link_textField;
	public JTextField limit_get_textField;
	public JButton run_button;
	public JButton stop_button;
	public JProgressBar progressBar;
	public WebLinkCheck wlc;
	public ButtonGroup browserGrp;

	public JRadioButton rdbtnFirefox;
	public JRadioButton rdbtnChrome;
	public JRadioButton rdbtnIe;
	public JRadioButton rdbtnOpera;

	public JCheckBox chk_link;
	public JCheckBox chk_get;
	public JCheckBox chk_recursive;
	public JCheckBox chk_level;
	public JTextField limit_recursive_textField;

	private static int progressnum;
	private JScrollPane scrollPane_1;
	private JTextArea result_text;

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame("WebLinkChecker");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(450, 450);
		mainFrame.setLocationRelativeTo(null);
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel", mainFrame);
		mainFrame.setVisible(true);
	}

	private static void setLookAndFeel(String laf, MainFrame mainframe) {
		try {
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(mainframe);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void result_update(String text) {
		JTextArea result_textarea = (JTextArea) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getComponent(0);
		result_textarea.append(text);
		result_textarea.repaint();
		Document doc = result_textarea.getDocument();
		result_textarea.setCaretPosition(doc.getLength());
	}

	public void csv_update(String text) {
		JTextArea csv_textarea = (JTextArea) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getComponent(0);
		csv_textarea.append(text);
		csv_textarea.repaint();
		Document doc = csv_textarea.getDocument();
		csv_textarea.setCaretPosition(doc.getLength());
		progressBar.setValue(progressnum++);
	}

	public MainFrame(String title) {
		this.setTitle(title);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 105, 101, 111, 89, 0 };
		gridBagLayout.rowHeights = new int[] { 19, 19, 21, 19, 19, 31, 110, 124, 21, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		// / -------------------- 結果 CSVテキストエリア -----------------------///


		scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 4;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 6;
		getContentPane().add(scrollPane_1, gbc_scrollPane_1);

		result_text = new JTextArea();
		result_text.setWrapStyleWord(true);
		result_text.setRows(1);
		result_text.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 11));
		result_text.setColumns(50);
		scrollPane_1.setViewportView(result_text);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		getContentPane().add(scrollPane, gbc_scrollPane);

		csv_text = new JTextArea();
		scrollPane.setViewportView(csv_text);
		csv_text.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 11));
		csv_text.setWrapStyleWord(true);
		csv_text.setColumns(50);
		csv_text.setRows(1);

		chk_recursive = new JCheckBox("再帰数");
		chk_recursive.setEnabled(false);
		chk_recursive.setToolTipText("スタートURLからの再帰回数の制限数");
		GridBagConstraints gbc_chk_recursive = new GridBagConstraints();
		gbc_chk_recursive.fill = GridBagConstraints.BOTH;
		gbc_chk_recursive.insets = new Insets(0, 0, 5, 5);
		gbc_chk_recursive.gridx = 2;
		gbc_chk_recursive.gridy = 3;
		getContentPane().add(chk_recursive, gbc_chk_recursive);

		limit_recursive_textField = new JTextField();
		limit_recursive_textField.setText("2");
		limit_recursive_textField.setColumns(10);
		GridBagConstraints gbc_limit_recursive_textField = new GridBagConstraints();
		gbc_limit_recursive_textField.insets = new Insets(0, 0, 5, 0);
		gbc_limit_recursive_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_limit_recursive_textField.gridx = 3;
		gbc_limit_recursive_textField.gridy = 3;
		getContentPane().add(limit_recursive_textField, gbc_limit_recursive_textField);

		chk_get = new JCheckBox("GET文字列種別数");
		chk_get.setEnabled(false);
		chk_get.setToolTipText("同じURLでGET文字列が違う場合、この数のGET文字列種類の分だけ行く");
		GridBagConstraints gbc_chk_get = new GridBagConstraints();
		gbc_chk_get.fill = GridBagConstraints.HORIZONTAL;
		gbc_chk_get.insets = new Insets(0, 0, 5, 5);
		gbc_chk_get.gridx = 0;
		gbc_chk_get.gridy = 4;
		getContentPane().add(chk_get, gbc_chk_get);

		limit_get_textField = new JTextField();
		limit_get_textField.setEnabled(false);
		limit_get_textField.setText("5");
		limit_get_textField.setColumns(10);
		GridBagConstraints gbc_limit_get_textField = new GridBagConstraints();
		gbc_limit_get_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_limit_get_textField.anchor = GridBagConstraints.NORTH;
		gbc_limit_get_textField.insets = new Insets(0, 0, 5, 5);
		gbc_limit_get_textField.gridx = 1;
		gbc_limit_get_textField.gridy = 4;
		getContentPane().add(limit_get_textField, gbc_limit_get_textField);

		// / -------------------- 基本設定 -----------------------///
		JLabel lblUrl = new JLabel("スタートURL");
		GridBagConstraints gbc_lblUrl = new GridBagConstraints();
		gbc_lblUrl.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblUrl.gridx = 0;
		gbc_lblUrl.gridy = 0;
		getContentPane().add(lblUrl, gbc_lblUrl);

		URL_textfield = new JTextField();
		URL_textfield.setText("http://www");
		URL_textfield.setHorizontalAlignment(SwingConstants.LEFT);
		URL_textfield.setColumns(10);
		GridBagConstraints gbc_URL_textfield = new GridBagConstraints();
		gbc_URL_textfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_URL_textfield.anchor = GridBagConstraints.NORTH;
		gbc_URL_textfield.insets = new Insets(0, 0, 5, 0);
		gbc_URL_textfield.gridwidth = 3;
		gbc_URL_textfield.gridx = 1;
		gbc_URL_textfield.gridy = 0;
		getContentPane().add(URL_textfield, gbc_URL_textfield);

		JLabel lblNg = new JLabel("NGワード(正規表現)");
		lblNg.setToolTipText("正規表現でリンク先アドレスにこの値が入っている場合、そこに行かないようにします");
		GridBagConstraints gbc_lblNg = new GridBagConstraints();
		gbc_lblNg.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNg.insets = new Insets(0, 0, 5, 5);
		gbc_lblNg.gridx = 0;
		gbc_lblNg.gridy = 1;
		getContentPane().add(lblNg, gbc_lblNg);

		NG_textField = new JTextField();
		NG_textField.setColumns(10);
		GridBagConstraints gbc_NG_textField = new GridBagConstraints();
		gbc_NG_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_NG_textField.insets = new Insets(0, 0, 5, 0);
		gbc_NG_textField.gridwidth = 3;
		gbc_NG_textField.gridx = 1;
		gbc_NG_textField.gridy = 1;
		getContentPane().add(NG_textField, gbc_NG_textField);

		JLabel label_4 = new JLabel("保存フォルダ");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 2;
		getContentPane().add(label_4, gbc_label_4);

		PIC_textField = new JTextField();
		PIC_textField.setText("C:\\snapshot");
		PIC_textField.setColumns(10);
		GridBagConstraints gbc_PIC_textField = new GridBagConstraints();
		gbc_PIC_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_PIC_textField.anchor = GridBagConstraints.NORTH;
		gbc_PIC_textField.insets = new Insets(0, 0, 5, 5);
		gbc_PIC_textField.gridwidth = 2;
		gbc_PIC_textField.gridx = 1;
		gbc_PIC_textField.gridy = 2;
		getContentPane().add(PIC_textField, gbc_PIC_textField);

		JButton button_2 = new JButton("参照");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fl_dlg = new JFileChooser();
				fl_dlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = fl_dlg.showOpenDialog(getParent());
				if (selected == JFileChooser.APPROVE_OPTION) {
					File file = fl_dlg.getCurrentDirectory();
					PIC_textField.setText(file.getAbsolutePath());
				}
			}
		});
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_2.anchor = GridBagConstraints.NORTH;
		gbc_button_2.insets = new Insets(0, 0, 5, 0);
		gbc_button_2.gridx = 3;
		gbc_button_2.gridy = 2;
		getContentPane().add(button_2, gbc_button_2);

		// / -------------------- 制限設定 -----------------------///
		limit_link_textField = new JTextField();
		limit_link_textField.setText("1000");
		limit_link_textField.setColumns(10);
		GridBagConstraints gbc_limit_link_textField = new GridBagConstraints();
		gbc_limit_link_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_limit_link_textField.anchor = GridBagConstraints.NORTH;
		gbc_limit_link_textField.insets = new Insets(0, 0, 5, 5);
		gbc_limit_link_textField.gridx = 1;
		gbc_limit_link_textField.gridy = 3;
		getContentPane().add(limit_link_textField, gbc_limit_link_textField);

		limit_level_textField = new JTextField();
		limit_level_textField.setEnabled(false);
		limit_level_textField.setText("5");
		limit_level_textField.setColumns(10);
		GridBagConstraints gbc_limit_level_textField = new GridBagConstraints();
		gbc_limit_level_textField.anchor = GridBagConstraints.NORTH;
		gbc_limit_level_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_limit_level_textField.insets = new Insets(0, 0, 5, 0);
		gbc_limit_level_textField.gridx = 3;
		gbc_limit_level_textField.gridy = 4;
		getContentPane().add(limit_level_textField, gbc_limit_level_textField);

		chk_link = new JCheckBox("リンク数");
		chk_link.setEnabled(false);
		chk_link.setToolTipText("リンクの総数においてこの数以上のリンクは移動しない");
		GridBagConstraints gbc_chk_link = new GridBagConstraints();
		gbc_chk_link.fill = GridBagConstraints.HORIZONTAL;
		gbc_chk_link.insets = new Insets(0, 0, 5, 5);
		gbc_chk_link.gridx = 0;
		gbc_chk_link.gridy = 3;
		getContentPane().add(chk_link, gbc_chk_link);

		chk_level = new JCheckBox("階層制限数");
		chk_level.setEnabled(false);
		chk_level.setToolTipText("スタートURLから/区切りで数値の以下の階層へのみ移動する");
		GridBagConstraints gbc_chk_level = new GridBagConstraints();
		gbc_chk_level.fill = GridBagConstraints.HORIZONTAL;
		gbc_chk_level.anchor = GridBagConstraints.NORTH;
		gbc_chk_level.insets = new Insets(0, 0, 5, 5);
		gbc_chk_level.gridx = 2;
		gbc_chk_level.gridy = 4;
		getContentPane().add(chk_level, gbc_chk_level);

		// / -------------------- ブラウザ設定 -----------------------///
		JLabel label_5 = new JLabel("ブラウザ");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_5.insets = new Insets(0, 0, 5, 5);
		gbc_label_5.gridx = 0;
		gbc_label_5.gridy = 5;
		getContentPane().add(label_5, gbc_label_5);

		rdbtnFirefox = new JRadioButton("Firefox");
		rdbtnChrome = new JRadioButton("Chrome");
		rdbtnIe = new JRadioButton("InternetExprorer");
		rdbtnIe.setToolTipText("使用するにはセキュリティ設定で、すべてのゾーンの「保護モードを有効にする」を統一する");

		rdbtnFirefox.setSelected(true);

		ButtonGroup browserGrp = new ButtonGroup();
		browserGrp.add(rdbtnFirefox);
		browserGrp.add(rdbtnChrome);
		browserGrp.add(rdbtnIe);

		JPanel panel = new JPanel();
		panel.add(rdbtnFirefox);
		panel.add(rdbtnChrome);
		panel.add(rdbtnIe);

		// / -------------------- 実行中止ボタン プログレスバー -----------------------///
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 3;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		getContentPane().add(panel, gbc_panel);
		rdbtnOpera = new JRadioButton("Opera");
		browserGrp.add(rdbtnOpera);
		panel.add(rdbtnOpera);

		run_button = new JButton("実行");
		run_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				check_run();
			}
		});

		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 8;
		getContentPane().add(progressBar, gbc_progressBar);

		stop_button = new JButton("中止");
		stop_button.setEnabled(false);
		stop_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wlc.interrupt();
				result_update("ユーザーによって中止されました\n");
				stopcheck();
			}
		});
		GridBagConstraints gbc_stop_button = new GridBagConstraints();
		gbc_stop_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_stop_button.insets = new Insets(0, 0, 0, 5);
		gbc_stop_button.gridx = 2;
		gbc_stop_button.gridy = 8;
		getContentPane().add(stop_button, gbc_stop_button);
		GridBagConstraints gbc_run_button = new GridBagConstraints();
		gbc_run_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_run_button.anchor = GridBagConstraints.NORTH;
		gbc_run_button.gridx = 3;
		gbc_run_button.gridy = 8;
		getContentPane().add(run_button, gbc_run_button);
	}

	public void check_run() {
		run_button.setEnabled(false);
		stop_button.setEnabled(true);
		wlc = new WebLinkCheck();
		wlc.start(this);

		if (URL_textfield.getText().toString().equals("")) {
			// JOptionPane.showMessageDialog(null, "URLを指定して下さい");
		} else if (NG_textField.getText().toString().equals("")) {
			// JOptionPane.showMessageDialog(null, "NGワードを指定して下さい");
		} else {

		}
	}

	public void csvSave(String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write(csv_text.getText());
			out.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	public void stopcheck() {
		run_button.setEnabled(true);
		stop_button.setEnabled(false);
		progressnum = 0;
		progressBar.setValue(0);
	}
}
