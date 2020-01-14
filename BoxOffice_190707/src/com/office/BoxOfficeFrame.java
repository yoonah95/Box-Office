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

	// 동적인 작업을 위해 전역으로 놓은 컴포넌트들
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
		setTitle("한성 시네마");
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

		movieInfo = new MovieInfo("영화 없음");
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

		// 로그인 페이지
		JPanel jpLogin = new JPanel();
		jpLogin.setLayout(new GridLayout(4, 1));
		JPanel[] jpLoginPanels = new JPanel[4];
		jpLoginPanels[0] = new JPanel();
		jpLoginPanels[1] = new JPanel();
		jpLoginPanels[2] = new JPanel();
		jpLoginPanels[3] = new JPanel();
		JLabel hansungCinema = new JLabel("한성 시네마");
		hansungCinema.setFont(new Font("양재소슬체S", Font.PLAIN, 30));
		jpLoginPanels[0].add(hansungCinema);
		jpLoginPanels[1].add(new JLabel("아이디 "));
		jpLoginID = new JTextField(10);
		jpLoginPanels[1].add(jpLoginID);
		jpLoginPanels[2].add(new JLabel("비밀번호 "));
		jpLoginPass = new JPasswordField(10);
		jpLoginPanels[2].add(jpLoginPass);
		JButton btnLogin1 = new JButton("회원가입");
		JButton btnLogin2 = new JButton("로그인");
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
							jpProfileTextArea.setText("아이디: " + rs.getString(1) + "\n이름: " + rs.getString(2) + "\n성별: "
									+ rs.getString(4) + "\n생년월일: " + rs.getString(3) + "\n이메일: " + rs.getString(5)
									+ "\n회원등급: " + rs.getString(7));
						}
						card.show(c, "MainPage");
						return;

					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "입력하신 정보가 올바르지 않습니다.", "알림", JOptionPane.WARNING_MESSAGE);
			}

		});
		jpLoginPanels[3].add(btnLogin1);
		jpLoginPanels[3].add(btnLogin2);
		jpLogin.add(jpLoginPanels[0]);
		jpLogin.add(jpLoginPanels[1]);
		jpLogin.add(jpLoginPanels[2]);
		jpLogin.add(jpLoginPanels[3]);

		// 회원가입 페이지
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
		jpRegisterPanels[0].add(new JLabel("회원 가입"));
		jpRegisterPanels[1].add(new JLabel("이름 "));
		jpRegisterPanels[1].add(jpRegisterName);
		jpRegisterPanels[2].add(new JLabel("아이디 "));
		jpRegisterPanels[2].add(jpRegisterID);
		jpRegisterPanels[3].add(new JLabel("비밀번호 "));
		jpRegisterPanels[3].add(jpRegisterPassword);
		jpRegisterPanels[4].add(new JLabel("비밀번호 확인 "));
		jpRegisterPanels[4].add(jpRegisterPassword2);
		jpRegisterPanels[5].add(new JLabel("성별 "));
		ButtonGroup jpRegisterGroup = new ButtonGroup();
		jpRegisterMale = new JRadioButton("남");
		jpRegisterFemale = new JRadioButton("여");
		jpRegisterGroup.add(jpRegisterMale);
		jpRegisterGroup.add(jpRegisterFemale);
		jpRegisterPanels[5].add(jpRegisterMale);
		jpRegisterPanels[5].add(jpRegisterFemale);
		jpRegisterPanels[6].add(new JLabel("생년월일(예: 950519) "));
		jpRegisterPanels[6].add(jpRegisterBirth);
		jpRegisterPanels[7].add(new JLabel("이메일 "));
		jpRegisterPanels[7].add(jpRegisterEmail);
		JButton btnRegister1 = new JButton("취소");
		JButton btnRegister2 = new JButton("가입하기");
		btnRegister1.setName("Back");
		jpRegisterPanels[8].add(btnRegister1);
		jpRegisterPanels[8].add(btnRegister2);
		for (int i = 0; i < 9; i++)
			jpRegister.add(jpRegisterPanels[i]);

		// 로그인하면 보이는 메인 페이지
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
		JButton btnMain1 = new JButton("영화 예매");
		JButton btnMain2 = new JButton("회원 정보");
		btnMain1.setName("SelectMovie");
		btnMain2.setName("Profile");
		jpMainCenter.add(jpMainIcon1);
		jpMainCenter.add(jpMainIcon2);
		jpMainCenter.add(btnMain1);
		jpMainCenter.add(btnMain2);
		jpMain.add(jpMainCenter, BorderLayout.CENTER);

		// 영화 선택하는 페이지
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
		 * JButton btnSelectMovie1 = new JButton("알라딘"); JButton btnSelectMovie2 = new
		 * JButton("토이 스토리4"); JButton btnSelectMovie3 = new JButton("기생충"); JButton
		 * btnSelectMovie4 = new JButton("존 윅3: 파라벨룸");
		 * jpSelectMovieList.add(btnSelectMovie1);
		 * jpSelectMovieList.add(btnSelectMovie2);
		 * jpSelectMovieList.add(btnSelectMovie3);
		 * jpSelectMovieList.add(btnSelectMovie4);
		 */
		JButton btnSelectMovie5 = new JButton("뒤로가기");
		btnSelectMovie5.setName("Back");
		jpSelectMovie.add(jpSelectMovieList, BorderLayout.CENTER);
		jpSelectMovie.add(btnSelectMovie5, BorderLayout.SOUTH);

		// 영화 설명 페이지
		JPanel jpMovieInfo = new JPanel();
		jpMovieInfo.setLayout(new BorderLayout());
		JPanel jpMovieInfoNorth = new JPanel();
		jpMovieInfoNorth.add(new JLabel("영화 정보"), BorderLayout.NORTH);
		jpMovieInfo.add(jpMovieInfoNorth, BorderLayout.NORTH);
		JPanel jpMovieInfoCenter = new JPanel();
		jpMovieInfoPoster = new JLabel(new ImageIcon("img/poster.png"));
		jpMovieInfoPoster.setSize(125, 150);
		jpMovieInfoCenter.add(jpMovieInfoPoster, BorderLayout.CENTER);
		jpMovieInfoTextArea = new JTextArea(10, 20);
		jpMovieInfoTextArea.setLineWrap(true);
		jpMovieInfoTextArea.setEditable(false);
		jpMovieInfoCenter.add(new JScrollPane(jpMovieInfoTextArea));
		jpMovieInfoCenter.add(new JLabel("이 영화를 예매하시겠습니까?"));
		jpMovieInfo.add(jpMovieInfoCenter, BorderLayout.CENTER);
		JPanel jpMovieInfoSouth = new JPanel();
		JButton btnMovieInfo1 = new JButton("뒤로가기");
		JButton btnMovieInfo2 = new JButton("예매하기");
		btnMovieInfo1.setName("Back");
		btnMovieInfo2.setName("Preorder1");
		jpMovieInfoSouth.add(btnMovieInfo1);
		jpMovieInfoSouth.add(btnMovieInfo2);
		jpMovieInfo.add(jpMovieInfoSouth, BorderLayout.SOUTH);

		// 예매 페이지-1(나이, 날짜, 상영관)
		JPanel jpScreen = new JPanel();
		jpScreen.setLayout(new BorderLayout());
		JPanel jpScreenCenter = new JPanel();
		jpScreenCenter.setLayout(new GridLayout(3, 1));
		JPanel jpScreenCenterAge = new JPanel();
		ButtonGroup jpScreenGroup = new ButtonGroup();
		JRadioButton jpScreenAge1 = new JRadioButton("성인");
		JRadioButton jpScreenAge2 = new JRadioButton("청소년");
		JRadioButton jpScreenAge3 = new JRadioButton("어린이");
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
		jpScreenCenterDate.add(new JLabel("상영 날짜: "));
		Integer[] month = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		jpScreenList1 = new JComboBox<Integer>(month);
		jpScreenCenterDate.add(jpScreenList1);
		jpScreenList1.addActionListener(new DateListener(1));
		jpScreenCenterDate.add(new JLabel("월"));
		jpScreenList2 = new JComboBox<Integer>();
		jpScreenCenterDate.add(jpScreenList2);
		jpScreenList2.addActionListener(new DateListener(2));
		jpScreenCenterDate.add(new JLabel("일"));
		jpScreenCenter.add(jpScreenCenterDate);
		jpScreenCenterScreen = new JPanel();
		jpScreenCenterScreen.setVisible(false);
		jpScreenCenter.add(jpScreenCenterScreen);
		JPanel jpScreenSouth = new JPanel();
		JButton btnScreen1 = new JButton("뒤로가기");
		btnScreen1.setName("Back");
		jpScreenSouth.add(btnScreen1);
		jpScreen.add(jpScreenCenter, BorderLayout.CENTER);
		jpScreen.add(jpScreenSouth, BorderLayout.SOUTH);

		// 예매 페이지-2(좌석)
		jpSeat = new JPanel();
		jpSeat.setLayout(new BorderLayout());
		jpSeatCenter = new JPanel();
		jpSeat.add(new JScrollPane(jpSeatCenter), BorderLayout.CENTER);
		JPanel jpSeatSouth = new JPanel();
		JButton btnSeat1 = new JButton("뒤로가기");
		btnSeat1.setName("Back");
		jpSeatSouth.add(btnSeat1);
		jpSeat.add(jpSeatSouth, BorderLayout.SOUTH);

		// 간식 구매할건지 묻는 페이지
		JPanel jpAskSnack = new JPanel();
		jpAskSnack.setLayout(new BorderLayout());
		JPanel jpAskSnackCenter = new JPanel();
		JLabel jpAskSnackImg = new JLabel(new ImageIcon("img/snack.png"));
		jpAskSnackImg.setSize(160, 160);
		jpAskSnackCenter.add(jpAskSnackImg);
		jpAskSnackCenter.add(new JLabel("간식을 구매하시겠습니까?"));
		jpAskSnack.add(jpAskSnackCenter, BorderLayout.CENTER);
		JPanel jpAskSnackSouth = new JPanel();
		JButton btnAskSnack1 = new JButton("뒤로가기");
		JButton btnAskSnack2 = new JButton("간식구매");
		JButton btnAskSnack3 = new JButton("건너뛰기");
		btnAskSnack1.setName("Back");
		btnAskSnack2.setName("BuySnack");
		btnAskSnack3.setName("Total");
		jpAskSnackSouth.add(btnAskSnack1);
		jpAskSnackSouth.add(btnAskSnack2);
		jpAskSnackSouth.add(btnAskSnack3);
		jpAskSnack.add(jpAskSnackSouth, BorderLayout.SOUTH);

		// 간식 구매 페이지
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
					JButton btnSnack = new JButton(snackName + " " + snackPrice + " 원");
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
		JButton btnBuySnack1 = new JButton("뒤로가기");
		JButton btnBuySnack2 = new JButton("선택완료");
		btnBuySnack1.setName("Back");
		btnBuySnack2.setName("TotalWithSnack");
		jpBuySnackSouth.add(btnBuySnack1);
		jpBuySnackSouth.add(btnBuySnack2);
		jpBuySnack.add(jpBuySnackSouth, BorderLayout.SOUTH);

		// 총정리
		JPanel jpTotal = new JPanel();
		jpTotal.setLayout(new BorderLayout());
		JPanel jpTotalCenter = new JPanel();
		jpTotalCenter.setLayout(new GridLayout(2, 1));
		jpTotalTextArea = new JTextArea();
		jpTotalTextArea.setEditable(false);
		jpTotalCenter.add(jpTotalTextArea);
		JPanel jpTotalPaymentMethod = new JPanel();
		ButtonGroup jpTotalGroup = new ButtonGroup();
		JRadioButton jpTotalCash = new JRadioButton("현금 결제");
		JRadioButton jpTotalCard = new JRadioButton("카드 결제");
		jpTotalGroup.add(jpTotalCard);
		jpTotalGroup.add(jpTotalCard);
		jpTotalCash.addActionListener(new PaymentMethodListener());
		jpTotalCard.addActionListener(new PaymentMethodListener());
		jpTotalPaymentMethod.add(jpTotalCash);
		jpTotalPaymentMethod.add(jpTotalCard);
		jpTotalCenter.add(jpTotalPaymentMethod);
		jpTotal.add(jpTotalCenter, BorderLayout.CENTER);
		JPanel jpTotalSouth = new JPanel();
		JButton btnTotal1 = new JButton("뒤로가기");
		JButton btnTotal2 = new JButton("예매하기");
		JButton btnTotal3 = new JButton("취소하기");
		btnTotal1.setName("Back");
		btnTotal2.setName("Summit");
		btnTotal3.setName("Home");
		jpTotalSouth.add(btnTotal1);
		jpTotalSouth.add(btnTotal2);
		jpTotalSouth.add(btnTotal3);
		jpTotal.add(jpTotalSouth, BorderLayout.SOUTH);

		// 사용자 정보, 예매 내역 확인하는 페이지
		JPanel jpProfile = new JPanel();
		jpProfile.setLayout(new BorderLayout());
		jpProfile.add(new JLabel("회원 정보"), BorderLayout.NORTH);
		JPanel jpProfileCenter = new JPanel();
		jpProfileCenter.setLayout(new GridLayout(2, 1));
		jpProfileTextArea = new JTextArea();
		jpProfileTextArea.setEditable(false);
		jpProfileCenter.add(jpProfileTextArea);
		jpProfilePreorders = new JPanel();
		jpProfilePreorders.setLayout(new WrapLayout());
		jpProfileCenter.add(new JScrollPane(jpProfilePreorders));
		jpProfile.add(jpProfileCenter, BorderLayout.CENTER);
		JButton btnProfile = new JButton("뒤로가기");
		btnProfile.setName("Home");
		jpProfile.add(btnProfile, BorderLayout.SOUTH);

		// Container에 JPanel 추가
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

		// 버튼들에 ActionListener 추가
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

	class PageListener implements ActionListener { // 페이지 전환 관련
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String name = btn.getName();
			if (name == null)
				handleError("페이지 이름이 null입니다");

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
				jpProfilePreorders.add(new JLabel("예매 내역"));
				for (int i = 0; i < len; i++) { // 예매 내역 보여주는 JPanel들 생성
					JPanel area = new JPanel();
					area.setBackground(Color.ORANGE);
					JTextArea textArea = new JTextArea(4, 30);
					textArea.setText(preorders.get(i).getPreorderInfo());
					textArea.setEditable(false);
					JButton btnDrop = new JButton("예매 취소");
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
					JOptionPane.showMessageDialog(null, "결제 방법을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
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
					JOptionPane.showMessageDialog(null, "상영예정인 영화입니다.", "알림", JOptionPane.WARNING_MESSAGE);
					return;
				}
			default:
				card.show(c, name);
			}
		}
	}

	class CancelListener implements ActionListener { // 누르면 예매 취소되는 버튼
		private PreorderInfo pi = null;

		public CancelListener(PreorderInfo pi) {
			this.pi = pi;
		}

		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			preorders.remove(pi); // preorders 에서 해당 예매내역 제거
			jpProfilePreorders.remove(btn.getParent()); // jpProfileorders에서 JPanel제거
			jpProfilePreorders.revalidate();
			jpProfilePreorders.repaint();
		}
	}

	class PaymentMethodListener implements ActionListener { // 결제 방법 선택
		public void actionPerformed(ActionEvent e) {
			JRadioButton jr = (JRadioButton) e.getSource();
			String value = jr.getText();
			ticketInfo.setPaymentMethod(value);
		}
	}

	class MovieListener implements ActionListener { // 영화 선택시 그 영화의 정보 가져옴
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String title = btn.getText();
			movieInfo = new MovieInfo(title);
			if (title == null)
				handleError("영화 제목이 null입니다.");

			jpMovieInfoPoster.setIcon(new ImageIcon(movieInfo.getImgPath()));
			jpMovieInfoTextArea.setText("");
			jpMovieInfoTextArea.append("제목: " + movieInfo.getTitle() + "\n");
			jpMovieInfoTextArea.append("장르: " + movieInfo.getGenre() + "\n");
			jpMovieInfoTextArea.append("줄거리: " + movieInfo.getSynopsis() + "\n");
			jpMovieInfoTextArea.append("관람 등급: " + movieInfo.getGrade() + "\n");
			jpMovieInfoTextArea.append("출연진: " + movieInfo.getCast() + "\n");
			jpMovieInfoTextArea.append("국가: " + movieInfo.getCountry() + "\n");
			jpMovieInfoTextArea.append("평점: " + movieInfo.getScore() + "/10.0\n");
			jpMovieInfoTextArea.append("영화 순위: " + movieInfo.getRanking() + "\n");

			nowMovie = title;
			card.show(c, "MovieInfo");
			c.repaint();
		}
	}

	class RegisterListener implements ActionListener { // 회원가입 조건 검사
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
				gender = "남";
			else if (jpRegisterFemale.isSelected())
				gender = "여";

			if (name.length() == 0 || id.length() == 0 || passString1.length() == 0 || passString2.length() == 0
					|| birth.length() == 0 || email.length() == 0 || gender.length() == 0) {
				JOptionPane.showMessageDialog(null, "비어있는 입력란이 있습니다.", "알림", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int len = users.size();
			for (int i = 0; i < len; i++) {
				UserInfo ui = users.get(i);
				if (ui.isIDAlreadyExist(id)) { // 아이디가 이미 존재하면 안 됨
					JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.", "알림", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			if (passString1.length() < 9) { // 비밀번호는 9자리 이상이어야 함
				JOptionPane.showMessageDialog(null, "비밀번호는 최소 9자리여야 합니다.", "알림", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!(passString1.equals(passString2))) { // 입력한 두 비밀번호가 다르면 안됨
				JOptionPane.showMessageDialog(null, "입력한 두 비밀번호가 맞지 않습니다.", "알림", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int registerEnd = JOptionPane.showConfirmDialog(null, "정말 회원가입을 하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION); // 모든
																														// 조건에
																														// 맞음
			if (registerEnd == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				signUp(id, name, birth, gender, email, String.valueOf(passString1), "화이트");

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

	class AgeListener implements ActionListener { // 나이 선택
		public void actionPerformed(ActionEvent e) {
			JRadioButton jr = (JRadioButton) e.getSource();
			String value = jr.getText();
			ticketInfo.setAge(value);
		}
	}

	class DateListener implements ActionListener { // 월 선택시 일 변경, 일 선택시 상영관 선택 버튼 나타남
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

		public void monthCheck() { // 월 선택시 선택 가능한 일 변경
			int selectedMonth = (int) jpScreenList1.getSelectedItem();
			DefaultComboBoxModel<Integer> model = null;
			if (selectedMonth == 4 || selectedMonth == 6 || selectedMonth == 9 || selectedMonth == 11) { // 30일
				Integer[] thirty = new Integer[30];
				for (int i = 0; i < 30; i++) {
					thirty[i] = i + 1;
				}
				model = new DefaultComboBoxModel<Integer>(thirty);
				jpScreenList2.setModel(model);
			} else if (selectedMonth == 2) { // 2월
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
			} else { // 31일
				Integer[] thirtyOne = new Integer[31];
				for (int i = 0; i < 31; i++) {
					thirtyOne[i] = i + 1;
				}
				model = new DefaultComboBoxModel<Integer>(thirtyOne);
				jpScreenList2.setModel(model);
			}
		}

		public void showScreens() { // 상영관 고르는 버튼 나타남
			jpScreenCenterScreen.setVisible(false);
			jpScreenCenterScreen.setVisible(true);
			JButton btn2d = new JButton("<html><body>2D<br>12:00~14:11<br>(131석)</body></html>");
			JButton btn3d = new JButton("<html><body>3D<br>13:00~15:11<br>(58석)</body></html>");
			JButton btn4d = new JButton("<html><body>4D<br>15:00~17:11<br>(84석)</body></html>");
			JButton btnIMAX = new JButton("<html><body>IMAX<br>19:30~21:41<br>(120석)</body></html>");
			btn2d.setName("12:00~14:11,2D,한성8관");
			btn3d.setName("13:00~15:11,3D,한성5관");
			btn4d.setName("15:00~17:11,4D,한성1관");
			btnIMAX.setName("19:30~21:41,IMAX,한성3관");
			jpScreenCenterScreen.removeAll(); // 이전의 버튼들 지움
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

	class ScreenListener implements ActionListener { // 상영관 좌석 선택 버튼이 나타남
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String input = btn.getName(); // 시간,상영방식,상영관이름
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
			int rowCharIndex = 0; // 27까지 가능
			for (int i = 0; i < seatNum[1]; i++) { // 누르면 좌석 선택되고 마지막 확인하는 화면으로 이동하는 버튼들 생성
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
						jpTotalTextArea.append("\n금액: " + feeInfo.total() + " 원");
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

		public int[] getScreenSeatNum(String screen) { // 세로, 총 좌석 수
			int[] seatNum = new int[3];
			switch (screen) {
			case "한성1관":
				seatNum[0] = 9;
				seatNum[1] = 84;
				break;
			case "한성2관":
				seatNum[0] = 19;
				seatNum[1] = 186;
				break;
			case "한성3관":
				seatNum[0] = 12;
				seatNum[1] = 120;
				break;
			case "한성4관":
				seatNum[0] = 24;
				seatNum[1] = 232;
				break;
			case "한성5관":
				seatNum[0] = 6;
				seatNum[1] = 58;
				break;
			case "한성6관":
				seatNum[0] = 9;
				seatNum[1] = 89;
				break;
			case "한성7관":
				seatNum[0] = 13;
				seatNum[1] = 126;
				break;
			case "한성8관":
				seatNum[0] = 14;
				seatNum[1] = 131;
				break;
			case "한성9관":
				seatNum[0] = 19;
				seatNum[1] = 187;
				break;
			case "한성10관":
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
			JButton btnDrop = new JButton("취소");
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

	class CancelListener2 implements ActionListener { // 누르면 간식 고른거 취소되는 버튼
		private SnackInfo si = null;
		private int orderId = 0;

		public CancelListener2(SnackInfo si,int orderId) {
			this.si = si;
			this.orderId = orderId;
			
		}

		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			selectedSnacks.remove(si); // selectedSnacks 에서 해당 예매내역 제거
			
			try {
				String query = "delete from ordersnack where order_id="+orderId;
				Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
			
				
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			
			
			jpBuySnackSelected.remove(btn.getParent()); // jpBuySnackSelected에서 JPanel제거
			jpBuySnackSelected.revalidate();
			jpBuySnackSelected.repaint();
		}
	}

	public static void main(String[] args) {
		new BoxOfficeFrame();
	}

}
