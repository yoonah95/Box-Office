package com.office;

import javax.swing.*;
import javax.swing.border.*;

import oracle.jdbc.pool.OracleDataSource;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class BoxOfficeFrame extends JFrame {
	private Container c = null;
	private CardLayout card = null;
	private MovieInfo movieInfo = null;
	private FeeInfo feeInfo = null;
	private TicketInfo ticketInfo = null;

	private Vector<SnackInfo> selectedSnacks = null;

	private Vector<UserInfo> users = null;
	private UserInfo currentUser = null;

	private Vector<PreorderInfo> preorders = null;

	// ������ �۾��� ���� �������� ���� ������Ʈ��
	private JTextField jpLoginID = null;
	private JPasswordField jpLoginPass = null;

	private JTextField jpRegisterID = null;
	private JTextField jpRegisterName = null;
	private JPasswordField jpRegisterPassword = null;
	private JPasswordField jpRegisterPassword2 = null;
	private JRadioButton jpRegisterMale = null;
	private JRadioButton jpRegisterFemale = null;
	private JTextField jpRegisterBirth = null;
	private JTextField jpRegisterEmail = null;

	private JLabel jpMainUserName = null;
	private JTextArea jpProfileTextArea = null;
	private JPanel jpProfilePreorders = null;
	private JLabel jpMovieInfoPoster = null;
	private JTextArea jpMovieInfoTextArea = null;
	JComboBox<Integer> jpScreenList1 = null;
	JComboBox<Integer> jpScreenList2 = null;
	private JPanel jpScreenCenterScreen = null;
	private JPanel jpSeat = null;
	private JPanel jpSeatCenter = null;
	private JPanel jpBuySnackSelected = null;
	private JTextArea jpTotalTextArea = null;
	private String nowMovie = null;

	private int snackId = 0;
	private int orderId = 0;
	private int ticketingId=0;
	OracleDataSource ods = null;
	Connection con = null;
	Statement stmt = null;
	String sql = null;

	ResultSet rs = null;

	private DB movieDB = new DB();

	public BoxOfficeFrame() {
		setTitle("�Ѽ� �ó׸�");
		setSize(550, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		c = getContentPane();
		card = new CardLayout();
		c.setLayout(card);

		users = new Vector<UserInfo>();
		currentUser = new UserInfo();
		preorders = new Vector<PreorderInfo>();

		selectedSnacks = new Vector<SnackInfo>();

		movieInfo = new MovieInfo("��ȭ ����");
		feeInfo = new FeeInfo();
		ticketInfo = new TicketInfo();

		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
			ods.setUser("wacher");
			ods.setPassword("qkrqk951");
			con = ods.getConnection();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// �α��� ������
		JPanel jpLogin = new JPanel();
		jpLogin.setLayout(new GridLayout(4, 1));
		JPanel[] jpLoginPanels = new JPanel[4];
		jpLoginPanels[0] = new JPanel();
		jpLoginPanels[1] = new JPanel();
		jpLoginPanels[2] = new JPanel();
		jpLoginPanels[3] = new JPanel();
		JLabel hansungCinema = new JLabel("�Ѽ� �ó׸�");
		hansungCinema.setFont(new Font("����ҽ�üS", Font.PLAIN, 30));
		jpLoginPanels[0].add(hansungCinema);
		jpLoginPanels[1].add(new JLabel("���̵� "));
		jpLoginID = new JTextField(10);
		jpLoginPanels[1].add(jpLoginID);
		jpLoginPanels[2].add(new JLabel("��й�ȣ "));
		jpLoginPass = new JPasswordField(10);
		jpLoginPanels[2].add(jpLoginPass);
		JButton btnLogin1 = new JButton("ȸ������");
		JButton btnLogin2 = new JButton("�α���");
		btnLogin1.setName("Register");
		btnLogin2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int len = users.size();
				String id = jpLoginID.getText();
				String pw = String.valueOf(jpLoginPass.getPassword());

				try {
					String query = "select * from member where member_id='" + id + "' and password='" + pw + "'";
					Statement statement = con.createStatement();
					ResultSet resultSet = statement.executeQuery(query);
					Boolean isLogin = false;
					while (resultSet.next()) {
						isLogin = true;
					}

					if (isLogin) {
						String query2 = "select * from member where member_id='" + id + "'";
						ResultSet rs = statement.executeQuery(query2);

						while (rs.next()) {
							jpMainUserName.setText(rs.getString(2));
							jpProfileTextArea.setText("���̵�: " + rs.getString(1) + "\n�̸�: " + rs.getString(2) + "\n����: "
									+ rs.getString(4) + "\n�������: " + rs.getString(3) + "\n�̸���: " + rs.getString(5)
									+ "\nȸ�����: " + rs.getString(7));
						}
						card.show(c, "MainPage");
						return;

					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "�Է��Ͻ� ������ �ùٸ��� �ʽ��ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
			}

		});
		jpLoginPanels[3].add(btnLogin1);
		jpLoginPanels[3].add(btnLogin2);
		jpLogin.add(jpLoginPanels[0]);
		jpLogin.add(jpLoginPanels[1]);
		jpLogin.add(jpLoginPanels[2]);
		jpLogin.add(jpLoginPanels[3]);

		// ȸ������ ������
		JPanel jpRegister = new JPanel();
		jpRegister.setLayout(new GridLayout(9, 1));
		JPanel[] jpRegisterPanels = new JPanel[9];
		for (int i = 0; i < 9; i++)
			jpRegisterPanels[i] = new JPanel();
		jpRegisterID = new JTextField(10);
		jpRegisterName = new JTextField(6);
		jpRegisterPassword = new JPasswordField(10);
		jpRegisterPassword2 = new JPasswordField(10);
		jpRegisterBirth = new JTextField(10);
		jpRegisterEmail = new JTextField(16);
		jpRegisterPanels[0].add(new JLabel("ȸ�� ����"));
		jpRegisterPanels[1].add(new JLabel("�̸� "));
		jpRegisterPanels[1].add(jpRegisterName);
		jpRegisterPanels[2].add(new JLabel("���̵� "));
		jpRegisterPanels[2].add(jpRegisterID);
		jpRegisterPanels[3].add(new JLabel("��й�ȣ "));
		jpRegisterPanels[3].add(jpRegisterPassword);
		jpRegisterPanels[4].add(new JLabel("��й�ȣ Ȯ�� "));
		jpRegisterPanels[4].add(jpRegisterPassword2);
		jpRegisterPanels[5].add(new JLabel("���� "));
		ButtonGroup jpRegisterGroup = new ButtonGroup();
		jpRegisterMale = new JRadioButton("��");
		jpRegisterFemale = new JRadioButton("��");
		jpRegisterGroup.add(jpRegisterMale);
		jpRegisterGroup.add(jpRegisterFemale);
		jpRegisterPanels[5].add(jpRegisterMale);
		jpRegisterPanels[5].add(jpRegisterFemale);
		jpRegisterPanels[6].add(new JLabel("�������(��: 950519) "));
		jpRegisterPanels[6].add(jpRegisterBirth);
		jpRegisterPanels[7].add(new JLabel("�̸��� "));
		jpRegisterPanels[7].add(jpRegisterEmail);
		JButton btnRegister1 = new JButton("���");
		JButton btnRegister2 = new JButton("�����ϱ�");
		btnRegister1.setName("Back");
		jpRegisterPanels[8].add(btnRegister1);
		jpRegisterPanels[8].add(btnRegister2);
		for (int i = 0; i < 9; i++)
			jpRegister.add(jpRegisterPanels[i]);

		// �α����ϸ� ���̴� ���� ������
		JPanel jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());
		JPanel jpMainNorth = new JPanel();
		jpMainUserName = new JLabel();
		jpMainNorth.add(jpMainUserName);
		jpMain.add(jpMainNorth, BorderLayout.NORTH);
		JPanel jpMainCenter = new JPanel();
		jpMainCenter.setLayout(new GridLayout(2, 2, 5, 5));
		JLabel jpMainIcon1 = new JLabel(new ImageIcon("img/movie.png"));
		JLabel jpMainIcon2 = new JLabel(new ImageIcon("img/profile.png"));
		jpMainIcon1.setSize(160, 160);
		jpMainIcon2.setSize(160, 160);
		JButton btnMain1 = new JButton("��ȭ ����");
		JButton btnMain2 = new JButton("ȸ�� ����");
		btnMain1.setName("SelectMovie");
		btnMain2.setName("Profile");
		jpMainCenter.add(jpMainIcon1);
		jpMainCenter.add(jpMainIcon2);
		jpMainCenter.add(btnMain1);
		jpMainCenter.add(btnMain2);
		jpMain.add(jpMainCenter, BorderLayout.CENTER);

		// ��ȭ �����ϴ� ������
		JPanel jpSelectMovie = new JPanel();
		jpSelectMovie.setLayout(new BorderLayout());
		JPanel jpSelectMovieList = new JPanel();
		jpSelectMovieList.setLayout(new GridLayout(4, 1));

		ArrayList<String> list = movieDB.movieList();
		for (int i = 0; i < list.size(); i++) {

			JButton btnSelectMovie1 = new JButton(list.get(i));
			jpSelectMovieList.add(btnSelectMovie1);
			btnSelectMovie1.addActionListener(new MovieListener());

		}
		/*
		 * JButton btnSelectMovie1 = new JButton("�˶��"); JButton btnSelectMovie2 = new
		 * JButton("���� ���丮4"); JButton btnSelectMovie3 = new JButton("�����"); JButton
		 * btnSelectMovie4 = new JButton("�� ��3: �Ķ󺧷�");
		 * jpSelectMovieList.add(btnSelectMovie1);
		 * jpSelectMovieList.add(btnSelectMovie2);
		 * jpSelectMovieList.add(btnSelectMovie3);
		 * jpSelectMovieList.add(btnSelectMovie4);
		 */
		JButton btnSelectMovie5 = new JButton("�ڷΰ���");
		btnSelectMovie5.setName("Back");
		jpSelectMovie.add(jpSelectMovieList, BorderLayout.CENTER);
		jpSelectMovie.add(btnSelectMovie5, BorderLayout.SOUTH);

		// ��ȭ ���� ������
		JPanel jpMovieInfo = new JPanel();
		jpMovieInfo.setLayout(new BorderLayout());
		JPanel jpMovieInfoNorth = new JPanel();
		jpMovieInfoNorth.add(new JLabel("��ȭ ����"), BorderLayout.NORTH);
		jpMovieInfo.add(jpMovieInfoNorth, BorderLayout.NORTH);
		JPanel jpMovieInfoCenter = new JPanel();
		jpMovieInfoPoster = new JLabel(new ImageIcon("img/poster.png"));
		jpMovieInfoPoster.setSize(125, 150);
		jpMovieInfoCenter.add(jpMovieInfoPoster, BorderLayout.CENTER);
		jpMovieInfoTextArea = new JTextArea(10, 20);
		jpMovieInfoTextArea.setLineWrap(true);
		jpMovieInfoTextArea.setEditable(false);
		jpMovieInfoCenter.add(new JScrollPane(jpMovieInfoTextArea));
		jpMovieInfoCenter.add(new JLabel("�� ��ȭ�� �����Ͻðڽ��ϱ�?"));
		jpMovieInfo.add(jpMovieInfoCenter, BorderLayout.CENTER);
		JPanel jpMovieInfoSouth = new JPanel();
		JButton btnMovieInfo1 = new JButton("�ڷΰ���");
		JButton btnMovieInfo2 = new JButton("�����ϱ�");
		btnMovieInfo1.setName("Back");
		btnMovieInfo2.setName("Preorder1");
		jpMovieInfoSouth.add(btnMovieInfo1);
		jpMovieInfoSouth.add(btnMovieInfo2);
		jpMovieInfo.add(jpMovieInfoSouth, BorderLayout.SOUTH);

		// ���� ������-1(����, ��¥, �󿵰�)
		JPanel jpScreen = new JPanel();
		jpScreen.setLayout(new BorderLayout());
		JPanel jpScreenCenter = new JPanel();
		jpScreenCenter.setLayout(new GridLayout(3, 1));
		JPanel jpScreenCenterAge = new JPanel();
		ButtonGroup jpScreenGroup = new ButtonGroup();
		JRadioButton jpScreenAge1 = new JRadioButton("����");
		JRadioButton jpScreenAge2 = new JRadioButton("û�ҳ�");
		JRadioButton jpScreenAge3 = new JRadioButton("���");
		jpScreenGroup.add(jpScreenAge1);
		jpScreenGroup.add(jpScreenAge2);
		jpScreenGroup.add(jpScreenAge3);
		jpScreenAge1.addActionListener(new AgeListener());
		jpScreenAge2.addActionListener(new AgeListener());
		jpScreenAge3.addActionListener(new AgeListener());
		jpScreenCenterAge.add(jpScreenAge1);
		jpScreenCenterAge.add(jpScreenAge2);
		jpScreenCenterAge.add(jpScreenAge3);
		jpScreenCenter.add(jpScreenCenterAge);
		JPanel jpScreenCenterDate = new JPanel();
		jpScreenCenterDate.add(new JLabel("�� ��¥: "));
		Integer[] month = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		jpScreenList1 = new JComboBox<Integer>(month);
		jpScreenCenterDate.add(jpScreenList1);
		jpScreenList1.addActionListener(new DateListener(1));
		jpScreenCenterDate.add(new JLabel("��"));
		jpScreenList2 = new JComboBox<Integer>();
		jpScreenCenterDate.add(jpScreenList2);
		jpScreenList2.addActionListener(new DateListener(2));
		jpScreenCenterDate.add(new JLabel("��"));
		jpScreenCenter.add(jpScreenCenterDate);
		jpScreenCenterScreen = new JPanel();
		jpScreenCenterScreen.setVisible(false);
		jpScreenCenter.add(jpScreenCenterScreen);
		JPanel jpScreenSouth = new JPanel();
		JButton btnScreen1 = new JButton("�ڷΰ���");
		btnScreen1.setName("Back");
		jpScreenSouth.add(btnScreen1);
		jpScreen.add(jpScreenCenter, BorderLayout.CENTER);
		jpScreen.add(jpScreenSouth, BorderLayout.SOUTH);

		// ���� ������-2(�¼�)
		jpSeat = new JPanel();
		jpSeat.setLayout(new BorderLayout());
		jpSeatCenter = new JPanel();
		jpSeat.add(new JScrollPane(jpSeatCenter), BorderLayout.CENTER);
		JPanel jpSeatSouth = new JPanel();
		JButton btnSeat1 = new JButton("�ڷΰ���");
		btnSeat1.setName("Back");
		jpSeatSouth.add(btnSeat1);
		jpSeat.add(jpSeatSouth, BorderLayout.SOUTH);

		// ���� �����Ұ��� ���� ������
		JPanel jpAskSnack = new JPanel();
		jpAskSnack.setLayout(new BorderLayout());
		JPanel jpAskSnackCenter = new JPanel();
		JLabel jpAskSnackImg = new JLabel(new ImageIcon("img/snack.png"));
		jpAskSnackImg.setSize(160, 160);
		jpAskSnackCenter.add(jpAskSnackImg);
		jpAskSnackCenter.add(new JLabel("������ �����Ͻðڽ��ϱ�?"));
		jpAskSnack.add(jpAskSnackCenter, BorderLayout.CENTER);
		JPanel jpAskSnackSouth = new JPanel();
		JButton btnAskSnack1 = new JButton("�ڷΰ���");
		JButton btnAskSnack2 = new JButton("���ı���");
		JButton btnAskSnack3 = new JButton("�ǳʶٱ�");
		btnAskSnack1.setName("Back");
		btnAskSnack2.setName("BuySnack");
		btnAskSnack3.setName("Total");
		jpAskSnackSouth.add(btnAskSnack1);
		jpAskSnackSouth.add(btnAskSnack2);
		jpAskSnackSouth.add(btnAskSnack3);
		jpAskSnack.add(jpAskSnackSouth, BorderLayout.SOUTH);

		// ���� ���� ������
		JPanel jpBuySnack = new JPanel();
		jpBuySnack.setLayout(new BorderLayout());
		JPanel jpBuySnackCenter = new JPanel();
		jpBuySnackCenter.setLayout(new GridLayout(2, 1));
		JPanel jpBuySnackButtons = new JPanel();
		jpBuySnackButtons.setLayout(new GridLayout(6, 2));
		try {
			String query = "select * from snack";
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while(resultSet.next()) {
				
					String snackName = resultSet.getString(2);
					int snackPrice = resultSet.getInt(3);
					JButton btnSnack = new JButton(snackName + " " + snackPrice + " ��");
					jpBuySnackButtons.add(btnSnack);
					SnackInfo snackInfo = new SnackInfo(snackName,snackPrice);
					btnSnack.addActionListener(new SnackSelectListener(snackInfo));
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			
		}

		jpBuySnackCenter.add(jpBuySnackButtons);
		jpBuySnack.add(jpBuySnackCenter, BorderLayout.CENTER);
		jpBuySnackSelected = new JPanel();
		jpBuySnackSelected.setLayout(new WrapLayout());
		jpBuySnackCenter.add(new JScrollPane(jpBuySnackSelected));
		jpBuySnack.add(jpBuySnackCenter);
		JPanel jpBuySnackSouth = new JPanel();
		JButton btnBuySnack1 = new JButton("�ڷΰ���");
		JButton btnBuySnack2 = new JButton("���ÿϷ�");
		btnBuySnack1.setName("Back");
		btnBuySnack2.setName("TotalWithSnack");
		jpBuySnackSouth.add(btnBuySnack1);
		jpBuySnackSouth.add(btnBuySnack2);
		jpBuySnack.add(jpBuySnackSouth, BorderLayout.SOUTH);

		// ������
		JPanel jpTotal = new JPanel();
		jpTotal.setLayout(new BorderLayout());
		JPanel jpTotalCenter = new JPanel();
		jpTotalCenter.setLayout(new GridLayout(2, 1));
		jpTotalTextArea = new JTextArea();
		jpTotalTextArea.setEditable(false);
		jpTotalCenter.add(jpTotalTextArea);
		JPanel jpTotalPaymentMethod = new JPanel();
		ButtonGroup jpTotalGroup = new ButtonGroup();
		JRadioButton jpTotalCash = new JRadioButton("���� ����");
		JRadioButton jpTotalCard = new JRadioButton("ī�� ����");
		jpTotalGroup.add(jpTotalCard);
		jpTotalGroup.add(jpTotalCard);
		jpTotalCash.addActionListener(new PaymentMethodListener());
		jpTotalCard.addActionListener(new PaymentMethodListener());
		jpTotalPaymentMethod.add(jpTotalCash);
		jpTotalPaymentMethod.add(jpTotalCard);
		jpTotalCenter.add(jpTotalPaymentMethod);
		jpTotal.add(jpTotalCenter, BorderLayout.CENTER);
		JPanel jpTotalSouth = new JPanel();
		JButton btnTotal1 = new JButton("�ڷΰ���");
		JButton btnTotal2 = new JButton("�����ϱ�");
		JButton btnTotal3 = new JButton("����ϱ�");
		btnTotal1.setName("Back");
		btnTotal2.setName("Summit");
		btnTotal3.setName("Home");
		jpTotalSouth.add(btnTotal1);
		jpTotalSouth.add(btnTotal2);
		jpTotalSouth.add(btnTotal3);
		jpTotal.add(jpTotalSouth, BorderLayout.SOUTH);

		// ����� ����, ���� ���� Ȯ���ϴ� ������
		JPanel jpProfile = new JPanel();
		jpProfile.setLayout(new BorderLayout());
		jpProfile.add(new JLabel("ȸ�� ����"), BorderLayout.NORTH);
		JPanel jpProfileCenter = new JPanel();
		jpProfileCenter.setLayout(new GridLayout(2, 1));
		jpProfileTextArea = new JTextArea();
		jpProfileTextArea.setEditable(false);
		jpProfileCenter.add(jpProfileTextArea);
		jpProfilePreorders = new JPanel();
		jpProfilePreorders.setLayout(new WrapLayout());
		jpProfileCenter.add(new JScrollPane(jpProfilePreorders));
		jpProfile.add(jpProfileCenter, BorderLayout.CENTER);
		JButton btnProfile = new JButton("�ڷΰ���");
		btnProfile.setName("Home");
		jpProfile.add(btnProfile, BorderLayout.SOUTH);

		// Container�� JPanel �߰�
		c.add(jpLogin, "LoginPage");
		c.add(jpRegister, "Register");
		c.add(jpMain, "MainPage");
		c.add(jpSelectMovie, "SelectMovie");
		c.add(jpMovieInfo, "MovieInfo");
		c.add(jpScreen, "Preorder1");
		c.add(jpSeat, "Preorder2");
		c.add(jpAskSnack, "AskSnack");
		c.add(jpBuySnack, "BuySnack");
		c.add(jpTotal, "Total");
		c.add(jpProfile, "Profile");

		PageListener pListener = new PageListener();
		MovieListener mListener = new MovieListener();
		RegisterListener rListener = new RegisterListener();

		// ��ư�鿡 ActionListener �߰�
		btnLogin1.addActionListener(pListener);
		btnRegister1.addActionListener(pListener);
		btnMain1.addActionListener(pListener);
		btnMain2.addActionListener(pListener);
		btnSelectMovie5.addActionListener(pListener);
		btnProfile.addActionListener(pListener);
		btnMovieInfo1.addActionListener(pListener);
		btnMovieInfo2.addActionListener(pListener);
		btnScreen1.addActionListener(pListener);
		btnSeat1.addActionListener(pListener);
		btnAskSnack1.addActionListener(pListener);
		btnAskSnack2.addActionListener(pListener);
		btnAskSnack3.addActionListener(pListener);
		btnBuySnack1.addActionListener(pListener);
		btnBuySnack2.addActionListener(pListener);
		btnTotal1.addActionListener(pListener);
		btnTotal2.addActionListener(pListener);
		btnTotal3.addActionListener(pListener);

		btnRegister2.addActionListener(rListener);

		setVisible(true);
	}

	public void handleError(String msg) {
		System.out.println(msg);
		System.exit(1);
	}

	class PageListener implements ActionListener { // ������ ��ȯ ����
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String name = btn.getName();
			if (name == null)
				handleError("������ �̸��� null�Դϴ�");

			switch (name) {
			case "Back":
				card.previous(c);
				break;
			case "Home":
				card.show(c, "MainPage");
				break;
			case "Profile":
				int len = preorders.size();
				jpProfilePreorders.removeAll();
				jpProfilePreorders.add(new JLabel("���� ����"));
				for (int i = 0; i < len; i++) { // ���� ���� �����ִ� JPanel�� ����
					JPanel area = new JPanel();
					area.setBackground(Color.ORANGE);
					JTextArea textArea = new JTextArea(4, 30);
					textArea.setText(preorders.get(i).getPreorderInfo());
					textArea.setEditable(false);
					JButton btnDrop = new JButton("���� ���");
					area.add(textArea);
					area.add(btnDrop);
					btnDrop.addActionListener(new CancelListener(preorders.get(i)));
					jpProfilePreorders.add(area);
				}
				card.show(c, name);
				break;
			case "TotalWithSnack":
				int snackLen = selectedSnacks.size();
				for (int i = 0; i < snackLen; i++) {
					String snackName = selectedSnacks.get(i).getSnackName();
					String snackPrice = Integer.toString(selectedSnacks.get(i).getSnackPrice());
					jpTotalTextArea.append("\n" + snackName + " " + snackPrice);
				}

				card.show(c, "Total");
				break;
			case "Summit":
				if (ticketInfo.getPaymentMethod()==null) {
					JOptionPane.showMessageDialog(null, "���� ����� �����ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
					return;
				}
				PreorderInfo pr = new PreorderInfo(ticketInfo, currentUser.getUserID());
				preorders.add(pr);
				
				ticketInfo = new TicketInfo();
				selectedSnacks = new Vector<SnackInfo>();
				
				try {
					Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					String query3 = "insert into ticketing values"+"(ticketing_id.nextVal,'"+orderId+"',1)";
					ResultSet rs = statement.executeQuery(query3);
					
					String query4 = "select ticketing_id from ticketing";
					rs = statement.executeQuery(query4);
					rs.last();
					ticketingId = rs.getInt(1);

					
				}catch(SQLException e3){
					e3.printStackTrace();
					
				}
				
				jpBuySnackSelected.removeAll();
	            jpBuySnackSelected.revalidate();
	            jpBuySnackSelected.repaint();
	            
	            
	            
	            
	            
				card.show(c, "MainPage");
				break;

			case "Preorder1":
				if (movieDB.movieShow(nowMovie)) {
					System.out.println(nowMovie);
					card.show(c, name);
				} else {
					JOptionPane.showMessageDialog(null, "�󿵿����� ��ȭ�Դϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
					return;
				}
			default:
				card.show(c, name);
			}
		}
	}

	class CancelListener implements ActionListener { // ������ ���� ��ҵǴ� ��ư
		private PreorderInfo pi = null;

		public CancelListener(PreorderInfo pi) {
			this.pi = pi;
		}

		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			preorders.remove(pi); // preorders ���� �ش� ���ų��� ����
			jpProfilePreorders.remove(btn.getParent()); // jpProfileorders���� JPanel����
			jpProfilePreorders.revalidate();
			jpProfilePreorders.repaint();
		}
	}

	class PaymentMethodListener implements ActionListener { // ���� ��� ����
		public void actionPerformed(ActionEvent e) {
			JRadioButton jr = (JRadioButton) e.getSource();
			String value = jr.getText();
			ticketInfo.setPaymentMethod(value);
		}
	}

	class MovieListener implements ActionListener { // ��ȭ ���ý� �� ��ȭ�� ���� ������
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String title = btn.getText();
			movieInfo = new MovieInfo(title);
			if (title == null)
				handleError("��ȭ ������ null�Դϴ�.");

			jpMovieInfoPoster.setIcon(new ImageIcon(movieInfo.getImgPath()));
			jpMovieInfoTextArea.setText("");
			jpMovieInfoTextArea.append("����: " + movieInfo.getTitle() + "\n");
			jpMovieInfoTextArea.append("�帣: " + movieInfo.getGenre() + "\n");
			jpMovieInfoTextArea.append("�ٰŸ�: " + movieInfo.getSynopsis() + "\n");
			jpMovieInfoTextArea.append("���� ���: " + movieInfo.getGrade() + "\n");
			jpMovieInfoTextArea.append("�⿬��: " + movieInfo.getCast() + "\n");
			jpMovieInfoTextArea.append("����: " + movieInfo.getCountry() + "\n");
			jpMovieInfoTextArea.append("����: " + movieInfo.getScore() + "/10.0\n");
			jpMovieInfoTextArea.append("��ȭ ����: " + movieInfo.getRanking() + "\n");

			nowMovie = title;
			card.show(c, "MovieInfo");
			c.repaint();
		}
	}

	class RegisterListener implements ActionListener { // ȸ������ ���� �˻�
		public void actionPerformed(ActionEvent e) {
			String name = jpRegisterName.getText().trim();
			String id = jpRegisterID.getText().trim();
			char[] pass1 = jpRegisterPassword.getPassword();
			char[] pass2 = jpRegisterPassword2.getPassword();
			String passString1 = String.valueOf(pass1);
			String passString2 = String.valueOf(pass2);
			String birth = jpRegisterBirth.getText().trim();
			String email = jpRegisterEmail.getText().trim();
			String gender = "";
			if (jpRegisterMale.isSelected())
				gender = "��";
			else if (jpRegisterFemale.isSelected())
				gender = "��";

			if (name.length() == 0 || id.length() == 0 || passString1.length() == 0 || passString2.length() == 0
					|| birth.length() == 0 || email.length() == 0 || gender.length() == 0) {
				JOptionPane.showMessageDialog(null, "����ִ� �Է¶��� �ֽ��ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int len = users.size();
			for (int i = 0; i < len; i++) {
				UserInfo ui = users.get(i);
				if (ui.isIDAlreadyExist(id)) { // ���̵� �̹� �����ϸ� �� ��
					JOptionPane.showMessageDialog(null, "�̹� �����ϴ� ���̵��Դϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			if (passString1.length() < 9) { // ��й�ȣ�� 9�ڸ� �̻��̾�� ��
				JOptionPane.showMessageDialog(null, "��й�ȣ�� �ּ� 9�ڸ����� �մϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!(passString1.equals(passString2))) { // �Է��� �� ��й�ȣ�� �ٸ��� �ȵ�
				JOptionPane.showMessageDialog(null, "�Է��� �� ��й�ȣ�� ���� �ʽ��ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int registerEnd = JOptionPane.showConfirmDialog(null, "���� ȸ�������� �Ͻðڽ��ϱ�?", "�˸�", JOptionPane.YES_NO_OPTION); // ���
																														// ���ǿ�
																														// ����
			if (registerEnd == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "ȸ�������� �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				signUp(id, name, birth, gender, email, String.valueOf(passString1), "ȭ��Ʈ");

				card.previous(c);
			}
		}

		public void signUp(String id, String name, String birth, String gender, String email, String password,
				String member_level) {
			try {
				String query = "insert into member values('" + id + "','" + name + "','" + birth + "','" + gender
						+ "','" + email + "','" + password + "','" + member_level + "')";
				Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	class AgeListener implements ActionListener { // ���� ����
		public void actionPerformed(ActionEvent e) {
			JRadioButton jr = (JRadioButton) e.getSource();
			String value = jr.getText();
			ticketInfo.setAge(value);
		}
	}

	class DateListener implements ActionListener { // �� ���ý� �� ����, �� ���ý� �󿵰� ���� ��ư ��Ÿ��
		private int num;

		public DateListener(int num) {
			this.num = num;
		}

		public void actionPerformed(ActionEvent e) {
			switch (num) {
			case 1:
				monthCheck();
				break;
			case 2:
				showScreens();
				break;
			}
		}

		public void monthCheck() { // �� ���ý� ���� ������ �� ����
			int selectedMonth = (int) jpScreenList1.getSelectedItem();
			DefaultComboBoxModel<Integer> model = null;
			if (selectedMonth == 4 || selectedMonth == 6 || selectedMonth == 9 || selectedMonth == 11) { // 30��
				Integer[] thirty = new Integer[30];
				for (int i = 0; i < 30; i++) {
					thirty[i] = i + 1;
				}
				model = new DefaultComboBoxModel<Integer>(thirty);
				jpScreenList2.setModel(model);
			} else if (selectedMonth == 2) { // 2��
				int thisYear = Calendar.YEAR;
				int leapYearCheck = 0;
				if (thisYear % 4 == 0)
					leapYearCheck++;
				if (thisYear % 100 != 0)
					leapYearCheck++;
				if (thisYear % 400 == 0)
					leapYearCheck++;

				if (leapYearCheck == 3) {
					Integer[] twentyNine = new Integer[29];
					for (int i = 0; i < 29; i++) {
						twentyNine[i] = i + 1;
					}
					model = new DefaultComboBoxModel<Integer>(twentyNine);
					jpScreenList2.setModel(model);
				} else {
					Integer[] twentyEight = new Integer[28];
					for (int i = 0; i < 28; i++) {
						twentyEight[i] = i + 1;
					}
					model = new DefaultComboBoxModel<Integer>(twentyEight);
					jpScreenList2.setModel(model);
				}
			} else { // 31��
				Integer[] thirtyOne = new Integer[31];
				for (int i = 0; i < 31; i++) {
					thirtyOne[i] = i + 1;
				}
				model = new DefaultComboBoxModel<Integer>(thirtyOne);
				jpScreenList2.setModel(model);
			}
		}

		public void showScreens() { // �󿵰� ���� ��ư ��Ÿ��
			jpScreenCenterScreen.setVisible(false);
			jpScreenCenterScreen.setVisible(true);
			JButton btn2d = new JButton("<html><body>2D<br>12:00~14:11<br>(131��)</body></html>");
			JButton btn3d = new JButton("<html><body>3D<br>13:00~15:11<br>(58��)</body></html>");
			JButton btn4d = new JButton("<html><body>4D<br>15:00~17:11<br>(84��)</body></html>");
			JButton btnIMAX = new JButton("<html><body>IMAX<br>19:30~21:41<br>(120��)</body></html>");
			btn2d.setName("12:00~14:11,2D,�Ѽ�8��");
			btn3d.setName("13:00~15:11,3D,�Ѽ�5��");
			btn4d.setName("15:00~17:11,4D,�Ѽ�1��");
			btnIMAX.setName("19:30~21:41,IMAX,�Ѽ�3��");
			jpScreenCenterScreen.removeAll(); // ������ ��ư�� ����
			jpScreenCenterScreen.add(btn2d);
			jpScreenCenterScreen.add(btn3d);
			jpScreenCenterScreen.add(btn4d);
			jpScreenCenterScreen.add(btnIMAX);
			btn2d.addActionListener(new ScreenListener());
			btn3d.addActionListener(new ScreenListener());
			btn4d.addActionListener(new ScreenListener());
			btnIMAX.addActionListener(new ScreenListener());
		}
	}

	class ScreenListener implements ActionListener { // �󿵰� �¼� ���� ��ư�� ��Ÿ��
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String input = btn.getName(); // �ð�,�󿵹��,�󿵰��̸�
			String[] split = input.split(",");
			ticketInfo.setTime(split[0]);
			ticketInfo.setScreen(split[1] + " " + split[2]);
			ticketInfo.setTitle(movieInfo.getTitle());
			ticketInfo.setGrade(movieInfo.getGrade());
			card.show(c, "Preorder2");

			int[] seatNum = getScreenSeatNum(split[2]);

			jpSeatCenter.removeAll();
			JPanel jpSeatCenterPanel = new JPanel();
			jpSeatCenterPanel.setLayout(new GridLayout(seatNum[0], 10));
			char[] rowChar = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
					'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a' };
			int rowCharIndex = 0; // 27���� ����
			for (int i = 0; i < seatNum[1]; i++) { // ������ �¼� ���õǰ� ������ Ȯ���ϴ� ȭ������ �̵��ϴ� ��ư�� ����
				JButton seat = new JButton(rowChar[rowCharIndex] + Integer.toString(i % 10 + 1));
				seat.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton btn = (JButton) e.getSource();
						String selectedSeat = btn.getText();
						ticketInfo.setSeat(selectedSeat);
						card.show(c, "AskSnack");
						String totalInfo = ticketInfo.getTicketInfo();
						jpTotalTextArea.setText(totalInfo);
						String screen = ticketInfo.getScreen().split(" ")[0];
						feeInfo.setAge(ticketInfo.getAge());
						feeInfo.setScreen(screen);
						jpTotalTextArea.append("\n�ݾ�: " + feeInfo.total() + " ��");
					}
				});
				jpSeatCenterPanel.add(seat);
				if (i % 10 == 9)
					rowCharIndex++;
			}
			jpSeatCenter.add(jpSeatCenterPanel);
			jpSeatCenter.revalidate();
			jpSeatCenter.repaint();
		}

		public int[] getScreenSeatNum(String screen) { // ����, �� �¼� ��
			int[] seatNum = new int[3];
			switch (screen) {
			case "�Ѽ�1��":
				seatNum[0] = 9;
				seatNum[1] = 84;
				break;
			case "�Ѽ�2��":
				seatNum[0] = 19;
				seatNum[1] = 186;
				break;
			case "�Ѽ�3��":
				seatNum[0] = 12;
				seatNum[1] = 120;
				break;
			case "�Ѽ�4��":
				seatNum[0] = 24;
				seatNum[1] = 232;
				break;
			case "�Ѽ�5��":
				seatNum[0] = 6;
				seatNum[1] = 58;
				break;
			case "�Ѽ�6��":
				seatNum[0] = 9;
				seatNum[1] = 89;
				break;
			case "�Ѽ�7��":
				seatNum[0] = 13;
				seatNum[1] = 126;
				break;
			case "�Ѽ�8��":
				seatNum[0] = 14;
				seatNum[1] = 131;
				break;
			case "�Ѽ�9��":
				seatNum[0] = 19;
				seatNum[1] = 187;
				break;
			case "�Ѽ�10��":
				seatNum[0] = 27;
				seatNum[1] = 263;
				break;
			default:
				seatNum[0] = 1;
				seatNum[1] = 10;
			}
			return seatNum;
		}
	}

	class SnackSelectListener implements ActionListener {
		private SnackInfo snackInfo = null;

		public SnackSelectListener(SnackInfo snackInfo) {
			this.snackInfo = snackInfo;
		}

		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String text = btn.getText();
			JPanel area = new JPanel();
			area.setBackground(Color.ORANGE);
			JTextArea textArea = new JTextArea(2, 15);
			textArea.setText(text);
			textArea.setEditable(false);
			JButton btnDrop = new JButton("���");
			area.add(textArea);
			area.add(btnDrop);
			selectedSnacks.add(snackInfo);
			
			String snackName = snackInfo.getSnackName();
			
			try {
				String query = "select snack_id from snack where category ='"+snackName+"'";
				Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet resultSet = statement.executeQuery(query);
				while(resultSet.next()) {
				snackId = resultSet.getInt(1);
				System.out.println(snackId);
				}
				String query2 = "insert into ordersnack values"+"(order_id.nextVal,'"+snackId+"',1)";
				ResultSet rs = statement.executeQuery(query2);
				
				String query3 = "select order_id from ordersnack";
				resultSet = statement.executeQuery(query3);
				resultSet.last();
				orderId = resultSet.getInt(1);
				System.out.println(orderId);
				
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			btnDrop.addActionListener(new CancelListener2(snackInfo,orderId));

			
			jpBuySnackSelected.add(area);
			jpBuySnackSelected.revalidate();
			jpBuySnackSelected.repaint();
		}
	}

	class CancelListener2 implements ActionListener { // ������ ���� ���� ��ҵǴ� ��ư
		private SnackInfo si = null;
		private int orderId = 0;

		public CancelListener2(SnackInfo si,int orderId) {
			this.si = si;
			this.orderId = orderId;
			
		}

		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			selectedSnacks.remove(si); // selectedSnacks ���� �ش� ���ų��� ����
			
			try {
				String query = "delete from ordersnack where order_id="+orderId;
				Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
			
				
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			
			
			jpBuySnackSelected.remove(btn.getParent()); // jpBuySnackSelected���� JPanel����
			jpBuySnackSelected.revalidate();
			jpBuySnackSelected.repaint();
		}
	}

	public static void main(String[] args) {
		new BoxOfficeFrame();
	}

}
