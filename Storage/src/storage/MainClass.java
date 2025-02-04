package storage;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
public class MainClass implements ActionListener,MouseListener
{	
	//Portatil
	String connectionString="jdbc:sqlserver://LAPTOP-J55A2J1I\\SQLEXPRESS;database=storage;trustServerCertificate=true;user=sa;password=CPtis2024";
	
	//Fixo
	//String connectionString="jdbc:sqlserver://DESKTOP-C774PUO;database=storage;trustServerCertificate=true;user=sa;password=CPtis2024";
	
	int loggedUser;
	
	public static final int background=0;
	public static final int foreground=1;
	public static final int selected=2;
	public static final int red=0;
	public static final int darkRed=1;
	
	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	int screenWidth=(int)screenSize.getWidth();
	int screenHeight=(int)screenSize.getHeight();
	
	public boolean loginSendSuccessful() {
		try(Connection c=DriverManager.getConnection(connectionString))
		{
			Statement stmt=c.createStatement();
			String checkUsers="select * from users";
			ResultSet rs=stmt.executeQuery(checkUsers);
			String user=inLoginUsername.getText();
			String pass=new String(inLoginPassword.getPassword());
			boolean invalid=true;
			
			if(!checkLoginFields()) {
				return false;
			}
			while(rs.next()) {
				
				invalid=false;
				
				if(!(user.equals(rs.getString("username")))) {
					lGeneralErrorLog.setText("Nome de utilizador ou palavra-passe incorretos");
					invalid=true;
					continue;
				}
				if(!(pass.equals(rs.getString("pass")))) {
					lGeneralErrorLog.setText("Palavra-passe incorreta");
					invalid=true;
					continue;
				}
				break;
			}
			if(invalid) {
				return false;
			}
			String query="select codUser from users where username='"+inLoginUsername.getText()+"'";
			rs=stmt.executeQuery(query);
			rs.next();
			loggedUser=Integer.parseInt(rs.getString("codUser"));
			return true;
		}
		catch(SQLException e) {
			StringWriter error=new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			errorLogTextArea.setText(error.toString());
			errorFrame.setVisible(true);
			return false;
		}
	}
	
	public boolean checkLoginFields() {
		if(inLoginUsername.getText().isEmpty()) {
			lUsernameErrorLog.setText("Campo obrigatório");
			return false;
		}
		
		if(inLoginPassword.getPassword().length<=0) {
			lPasswordErrorLog.setText("Campo obrigatório");
			return false;
		}
		return true;
	}
	
	public boolean signUpSendSuccessful() {
		if(checkSignUpFields()&&(!userExists())) {
			try(Connection c=DriverManager.getConnection(connectionString)) {
				Statement stmt=c.createStatement();
				String newSignUp="insert into users(username,pass,email,phone)values('"+inSignUpUsername.getText()+"','"+new String(inSignUpPassword.getPassword())+"','"+inSignUpEmail.getText()+"',"+Integer.parseInt(inSignUpPhone.getText())+");";
				stmt.executeUpdate(newSignUp);
				return true;
			}
			catch(SQLException e) {
				StringWriter error=new StringWriter();
				e.printStackTrace(new PrintWriter(error));
				errorLogTextArea.setText(error.toString());
				errorFrame.setVisible(true);
				return false;
			}
		}
		return false;
	}
	
	public boolean userExists() {
		try(Connection c=DriverManager.getConnection(connectionString))
		{
			Statement stmt=c.createStatement();
			String checkUsers="select * from users";
			ResultSet rs=stmt.executeQuery(checkUsers);
			while(rs.next()) {
				String user=inSignUpUsername.getText();
				String email=inSignUpEmail.getText();
				int phone=Integer.parseInt(inSignUpPhone.getText());
				
				if(user.equals(rs.getString("username"))) {
					suUsernameErrorLog.setText("Este nome de utilizador já existe!");
					return true;
				}
				
				if(email.equals(rs.getString("email"))) {
					suEmailErrorLog.setText("Este email já existe!");
					return true;
				}
				
				if(phone==rs.getInt("phone")) {
					suPhoneErrorLog.setText("Este número de telefone já existe!");
					return true;
				}
			}
			return false;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public boolean checkSignUpFields() {
		if(inSignUpUsername.getText().isEmpty()) {
			suUsernameErrorLog.setText("Campo obricatório");
			return false;
		}
		if(inSignUpUsername.getText().indexOf("'")>=0) {
			suUsernameErrorLog.setText("Nome de utilizador inválido");
			return false;
		}
		
		if(inSignUpPhone.getText().isEmpty()) {			
			suPhoneErrorLog.setText("Campo obricatório");
			return false;
		}
		if(!(inSignUpPhone.getText().matches(".*\\d.*"))) {
			suPhoneErrorLog.setText("Número de telefone inválido");
			return false;
		}
		if(inSignUpPhone.getText().length()>9) {
			suPhoneErrorLog.setText("Número de caractéres demasiado longo");
			return false;
		}
		
		if(inSignUpEmail.getText().isEmpty()) {
			suEmailErrorLog.setText("Campo obricatório");
			return false;
		}
		if(!(inSignUpEmail.getText().indexOf('@')>=0)||inSignUpEmail.getText().indexOf('@')==inSignUpEmail.getText().length()-1) {
			suEmailErrorLog.setText("Endereço de email inválido");
			return false;
		}
		
		if(inSignUpPassword.getPassword().length<=0) {
			suPasswordErrorLog.setText("Campo obricatório");
			return false;
		}
		return true;
	}
	
	public boolean userHasStorage() {
		try(Connection c=DriverManager.getConnection(connectionString)) {
			Statement stmt=c.createStatement();
			String checkStorages="select * from storageInfo";
			ResultSet rs=stmt.executeQuery(checkStorages);
			while(rs.next()) {
				if(loggedUser==Integer.parseInt(rs.getString("creator"))) {
					return true;
				}
			}
			return false;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;			
		}
	}
	
	public boolean storageSendSuccessful() {
		try(Connection c=DriverManager.getConnection(connectionString)) {
			Statement stmt=c.createStatement();
			String newStorage="insert into storageInfo(nome,comprimento,largura,numPisos,tipo,creator)values('"+inStorageName.getText()+"','"+Integer.parseInt(inStorageLength.getText())+"','"+Integer.parseInt(inStorageWidth.getText())+"',"+Integer.parseInt(inNumPisos.getText())+",'"+inStorageType.getSelectedIndex()+"',"+loggedUser+");";
			stmt.executeUpdate(newStorage);
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;			
		}
	}
	
	CardLayout c=new CardLayout();
	
	Color[] purpleTheme={new Color(222,215,224),new Color(108,91,120),new Color(88,71,100)};
	
	Color[] lightTheme={new Color(242,242,242),new Color(127,127,127),new Color(107,107,107)};
	
	Color[] lightBlueTheme={new Color(96,150,186),new Color(39,76,119),new Color(9,56,99)};
	
	Color[] darkBlueTheme={new Color(13,27,42),new Color(65,90,119),new Color(119,141,169)};
	
	Color[] greenTheme={new Color(173,177,138),new Color(58,90,64),new Color(52,78,65)};
	
	Color[] darkTheme={new Color(18,20,32),new Color(44,43,60),new Color(64,63,76)};
	
	Color[] reds={new Color(200,30,30),new Color(175,0,0)};
	
	
	String theme="darkBlueTheme";
	
	
	ImageIcon biggestLogo=new ImageIcon(theme.equals("lightTheme")?"MT STORAGE light.png":(theme.equals("darkTheme")?"MT STORAGE dark.png":(theme.equals("darkBlueTheme")?"MT STORAGE darkBlue.png":(theme.equals("lightBlueTheme")?"MT STORAGE lightBlue.png":(theme.equals("greenTheme")?"MT STORAGE green.png":"MT STORAGE.png")))));
	ImageIcon mediumLogo=new ImageIcon(theme.equals("lightTheme")?"MT STORAGE signUp light.png":(theme.equals("darkTheme")?"MT STORAGE signUp dark.png":(theme.equals("darkBlueTheme")?"MT STORAGE signUp darkBlue.png":(theme.equals("lightBlueTheme")?"MT STORAGE signUp lightBlue.png":(theme.equals("greenTheme")?"MT STORAGE signUp green.png":"MT STORAGE signUp.png")))));
	ImageIcon bigRoundedButton=new ImageIcon(theme.equals("lightTheme")?"rounded button 2 light.png":(theme.equals("darkTheme")?"rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"rounded button 2 green.png":"rounded button 2.png")))));
	ImageIcon darkerBigRoundedButton=new ImageIcon(theme.equals("lightTheme")?"darker rounded button 2 light.png":(theme.equals("darkTheme")?"darker rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"darker rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"darker rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"darker rounded button 2 green.png":"darker rounded button 2.png")))));
	ImageIcon smallRoundedButton=new ImageIcon(theme.equals("lightTheme")?"rounded button light.png":(theme.equals("darkTheme")?"rounded button dark.png":(theme.equals("darkBlueTheme")?"rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button lightBlue.png":(theme.equals("greenTheme")?"rounded button green.png":"rounded button.png")))));
	ImageIcon darkerSmallRoundedButton=new ImageIcon(theme.equals("lightTheme")?"darker rounded button light.png":(theme.equals("darkTheme")?"darker rounded button dark.png":(theme.equals("darkBlueTheme")?"darker rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"darker rounded button lightBlue.png":(theme.equals("greenTheme")?"darker rounded button green.png":"darker rounded button.png")))));
	ImageIcon seta=new ImageIcon(theme.equals("lightTheme")?"seta light.png":(theme.equals("darkTheme")?"seta dark.png":(theme.equals("darkBlueTheme")?"seta darkBlue.png":(theme.equals("lightBlueTheme")?"seta lightBlue.png":(theme.equals("greenTheme")?"seta green.png":"seta.png")))));
	ImageIcon darkerSeta=new ImageIcon(theme.equals("lightTheme")?"seta escura light.png":(theme.equals("darkTheme")?"seta escura dark.png":(theme.equals("darkBlueTheme")?"seta escura darkBlue.png":(theme.equals("lightBlueTheme")?"seta escura lightBlue.png":(theme.equals("greenTheme")?"seta escura green.png":"seta escura.png")))));
	ImageIcon hamburguerButton=new ImageIcon(theme.equals("lightTheme")?"Hamburguer button light.png":(theme.equals("darkTheme")?"Hamburguer button dark.png":(theme.equals("darkBlueTheme")?"Hamburguer button darkBlue.png":(theme.equals("lightBlueTheme")?"Hamburguer button lightBlue.png":(theme.equals("greenTheme")?"Hamburguer button green.png":"Hamburguer button purple.png")))));
	
	Color themedForeground=(theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground])))));
	Color themedBackground=(theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background])))));
	Color themedSelected=(theme.equals("lightTheme")?lightTheme[selected]:(theme.equals("darkTheme")?darkTheme[selected]:(theme.equals("darkBlueTheme")?darkBlueTheme[selected]:(theme.equals("lightBlueTheme")?lightBlueTheme[selected]:(theme.equals("greenTheme")?greenTheme[selected]:purpleTheme[selected])))));
	
	JFrame mainFrame=new JFrame("Storage");
	JPanel mainPanel=new JPanel();
	
		JPanel titlebarPanel=new JPanel();
			JButton close=new JButton(new ImageIcon("closeIcon colored transparent.png"));
			JButton menuButton=new JButton(hamburguerButton);
			
		JPanel hamburguerPanel=new JPanel();
			JButton account=new JButton("Conta");
			JButton themeButton=new JButton("Tema");
			
		
		JPanel chooseLoginPanel=new JPanel();
			JLabel chooseLoginPaneLogo=new JLabel(biggestLogo);
			JButton signUp=new JButton();
			JLabel signUpText=new JLabel("Criar conta");
			JButton login=new JButton();
			JLabel loginText=new JLabel("Login");
			
		JPanel signUpPanel=new JPanel();
			Border line=new LineBorder(themedForeground,2);
			Border empty=new EmptyBorder(0, 10, 0, 0);
			CompoundBorder border = new CompoundBorder(line, empty);
			JButton suVoltarChooseLogin=new JButton();
			JLabel signUpPaneLogo=new JLabel(mediumLogo);
			JLabel signUpPaneTitle=new JLabel("Criar conta");
			JLabel lSignUpUsername=new JLabel("Nome de utilizador");
			JTextField inSignUpUsername=new JTextField(15);
			JLabel suUsernameErrorLog=new JLabel();
			JLabel lSignUpPassword=new JLabel("Palavra - passe");
			JPasswordField inSignUpPassword=new JPasswordField(15);
			JLabel suPasswordErrorLog=new JLabel();
			JLabel lSignUpEmail=new JLabel("E-mail");
			JTextField inSignUpEmail=new JTextField(15);
			JLabel suEmailErrorLog=new JLabel();
			JLabel lSignUpPhone=new JLabel("Número de telefone");
			JTextField inSignUpPhone=new JTextField(15);
			JLabel suPhoneErrorLog=new JLabel();
			JLabel signUpSendText=new JLabel("Submeter");
			JButton signUpSend=new JButton();
		
		JPanel loginPanel=new JPanel();
			JButton lVoltarChooseLogin=new JButton();
			JLabel loginPaneLogo=new JLabel(mediumLogo);
			JLabel loginPaneTitle=new JLabel("Iniciar sessão");
			JLabel lLoginUsername=new JLabel("Nome de utilizador");
			JTextField inLoginUsername=new JTextField(15);
			JLabel lUsernameErrorLog=new JLabel();
			JLabel lLoginPassword=new JLabel("Palavra - passe");
			JPasswordField inLoginPassword=new JPasswordField(15);
			JLabel lPasswordErrorLog=new JLabel();
			JLabel lGeneralErrorLog=new JLabel();
			JLabel loginSendText=new JLabel("Entrar");
			JButton loginSend=new JButton();
		
		
		JPanel createStoragePanel=new JPanel();
			JLabel lStorageName=new JLabel("Nome da Storage");
			JTextField inStorageName=new JTextField(15);
			JLabel lStorageLength=new JLabel("Comprimento da Storage");
			JTextField inStorageLength=new JTextField(4);
			JLabel lStorageWidth=new JLabel("Largura da Storage");
			JTextField inStorageWidth=new JTextField(4);
			JLabel lNumPisos=new JLabel("Número de pisos da Storage");
			JTextField inNumPisos=new JTextField(2);
			JLabel lStorageType=new JLabel("Número de pisos da Storage");
			String[] tipos={"  Armazém","  Laboratório","  Moradia","  Outro"};
			JComboBox inStorageType=new JComboBox(tipos);
			JLabel storageSendText=new JLabel("Criar Storage");
			JButton storageSend=new JButton();
			
		JPanel mainStoragePanel=new JPanel();
			JPanel sidePanel=new JPanel();
			JTextField searchBar=new JTextField();
			JButton searchButton=new JButton();
	
	JPanel actualPanel=chooseLoginPanel;
	
	
	JFrame errorFrame=new JFrame();
		JButton errorBoxRemover=new JButton();
	
	JPanel errorTitlebarPanel=new JPanel();
		JLabel errorLogo=new JLabel(new ImageIcon("STORAGE Frame Icon tiny.png"));
		JButton errorClose=new JButton(new ImageIcon("closeIcon colored transparent.png"));
	
	JPanel errorPanel=new JPanel();
		JTextArea errorLogTextArea=new JTextArea();
		JButton confirmButton=new JButton("ok");
	
	public MainClass()
	{
		
		UIManager.put("Button.select",selected);
		mainFrame.setUndecorated(true);
		Image icon = Toolkit.getDefaultToolkit().getImage("STORAGE Frame Icon.png");  
		mainFrame.setIconImage(icon);  
		mainFrame.setSize(1366,768);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(mainPanel);

		titlebarPanel.setLayout(null);
		titlebarPanel.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		titlebarPanel.setBackground(themedForeground);
		titlebarPanel.add(close);
		close.setFocusable(false);
			close.setBounds(1309,0,57,57);
			close.setContentAreaFilled(false);
			close.setForeground(themedBackground);
			close.setBorder(null);
			close.addActionListener(this);
			close.addMouseListener(this);
		titlebarPanel.add(menuButton);
			menuButton.setFocusable(false);
			menuButton.setBounds(15,3,50,50);
			menuButton.setBorder(null);
			menuButton.setContentAreaFilled(false);
			menuButton.addActionListener(this);
			menuButton.addMouseListener(this);
		
		titlebarPanel.setVisible(true);
		
		
		mainPanel.setLayout(c);
		mainPanel.setBounds(0,57,1366,711);
		mainPanel.add("chooseLoginPanel",chooseLoginPanel);
		mainPanel.add("signUpPanel",signUpPanel);
		mainPanel.add("loginPanel",loginPanel);
		mainPanel.add("mainStoragePanel",mainStoragePanel);
		mainPanel.add("createStoragePanel",createStoragePanel);
		mainPanel.setVisible(true);
		
		
		actualPanel=chooseLoginPanel; 
		actualPanel.setBounds(0,57,1366,711);
		actualPanel.setLayout(null);
		actualPanel.setBackground(themedBackground);
		actualPanel.setLayout(null);
		actualPanel.add(chooseLoginPaneLogo);
			chooseLoginPaneLogo.setBounds(504,77,355,309);
		actualPanel.add(signUpText);
			signUpText.setBounds(502,453,361,57);
			signUpText.setFont(new Font("SF Pro Display",Font.BOLD,24));
			signUpText.setHorizontalAlignment(SwingConstants.CENTER);
			signUpText.setVerticalAlignment(SwingConstants.CENTER);
			signUpText.setForeground(themedBackground);
		actualPanel.add(signUp);
			signUp.setFocusable(false);
			signUp.setBounds(502,453,361,57);
			signUp.setContentAreaFilled(false);
			signUp.setOpaque(false);
			signUp.setIcon(bigRoundedButton);
			signUp.setBorder(null);
		actualPanel.add(loginText);
			loginText.setBounds(502,538,361,57);
			loginText.setFont(new Font("SF Pro Display",Font.BOLD,24));
			loginText.setForeground(themedBackground);
			loginText.setHorizontalAlignment(SwingConstants.CENTER);
			loginText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(login);
			login.setFocusable(false);
			login.setBounds(502,538,361,57);
			login.setBorder(null);
			login.setContentAreaFilled(false);
			login.setIcon(bigRoundedButton);
		
		signUp.addActionListener(this);
		signUp.addMouseListener(this);
		login.addActionListener(this);
		login.addMouseListener(this);
		
		
		actualPanel=signUpPanel;
		actualPanel.setLayout(null);
		actualPanel.setBackground(themedBackground);
		actualPanel.add(suVoltarChooseLogin);
			suVoltarChooseLogin.setIcon(seta);
			suVoltarChooseLogin.setBackground(null);
			suVoltarChooseLogin.setBorder(null);
			suVoltarChooseLogin.setBounds(83,33,42,41);
		actualPanel.add(signUpPaneLogo);
			signUpPaneLogo.setBounds(576,51,208,180);
		actualPanel.add(signUpPaneTitle);
			signUpPaneTitle.setBounds(531,267,306,57);
			signUpPaneTitle.setOpaque(true);
			signUpPaneTitle.setBorder(null);
			signUpPaneTitle.setBackground(themedForeground);
			signUpPaneTitle.setFont(new Font("SF Pro Display",Font.BOLD,24));
			signUpPaneTitle.setForeground(themedBackground);
			signUpPaneTitle.setHorizontalAlignment(SwingConstants.CENTER);
			signUpPaneTitle.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(lSignUpUsername);
			lSignUpUsername.setBounds(267,337,386,44);
			lSignUpUsername.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lSignUpUsername.setForeground(themedForeground);
		actualPanel.add(inSignUpUsername);
			inSignUpUsername.setBounds(254,372,386,44);
			inSignUpUsername.setMargin(new Insets(10,10,10,10));
			inSignUpUsername.setBorder(border);
			inSignUpUsername.setBackground(themedBackground);
			inSignUpUsername.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inSignUpUsername.setForeground((theme.equals("darkTheme")||theme.equals("darkBlueTheme"))?Color.white:null);
		actualPanel.add(suUsernameErrorLog);
			suUsernameErrorLog.setBounds(267,403,386,44);
			suUsernameErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lSignUpEmail);
			lSignUpEmail.setBounds(267,426,386,44);
			lSignUpEmail.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lSignUpEmail.setForeground(themedForeground);
		actualPanel.add(inSignUpEmail);
			inSignUpEmail.setBounds(254,461,386,44);
			inSignUpEmail.setMargin(new Insets(10,10,10,10));
			inSignUpEmail.setBorder(border);
			inSignUpEmail.setBackground(themedBackground);
			inSignUpEmail.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inSignUpEmail.setForeground((theme.equals("darkTheme")||theme.equals("darkBlueTheme"))?Color.white:null);
		actualPanel.add(suEmailErrorLog);
			suEmailErrorLog.setBounds(267,492,386,44);
			suEmailErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lSignUpPhone);
			lSignUpPhone.setBounds(742,337,386,44);
			lSignUpPhone.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lSignUpPhone.setForeground(themedForeground);
		actualPanel.add(inSignUpPhone);
			inSignUpPhone.setBounds(729,372,386,44);
			inSignUpPhone.setMargin(new Insets(10,10,10,10));
			inSignUpPhone.setBorder(border);
			inSignUpPhone.setBackground(themedBackground);
			inSignUpPhone.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inSignUpPhone.setForeground((theme.equals("darkTheme")||theme.equals("darkBlueTheme"))?Color.white:null);
		actualPanel.add(suPhoneErrorLog);
			suPhoneErrorLog.setBounds(742,403,386,44);
			suPhoneErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lSignUpPassword);
			lSignUpPassword.setBounds(742,426,386,44);
			lSignUpPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lSignUpPassword.setForeground(themedForeground);
		actualPanel.add(inSignUpPassword);
			inSignUpPassword.setBounds(729,461,386,44);
			inSignUpPassword.setMargin(new Insets(10,10,10,10));
			inSignUpPassword.setBorder(border);
			inSignUpPassword.setBackground(themedBackground);
			inSignUpPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			inSignUpPassword.setForeground((theme.equals("darkTheme")||theme.equals("darkBlueTheme"))?Color.white:null);
		actualPanel.add(suPasswordErrorLog);
			suPasswordErrorLog.setBounds(742,492,386,44);
			suPasswordErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(signUpSendText);
			signUpSendText.setBounds(531,551,306,57);
			signUpSendText.setFont(new Font("SF Pro Display",Font.BOLD,24));
			signUpSendText.setForeground(themedBackground);
			signUpSendText.setHorizontalAlignment(SwingConstants.CENTER);
			signUpSendText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(signUpSend);
			signUpSend.setFocusable(false);
			signUpSend.setBounds(531,551,306,57);
			signUpSend.setOpaque(false);
			signUpSend.setContentAreaFilled(false);
			signUpSend.setBorder(null);
			signUpSend.setIcon(smallRoundedButton);
		
		suVoltarChooseLogin.addActionListener(this);
		suVoltarChooseLogin.addMouseListener(this);
		signUpSend.addActionListener(this);
		signUpSend.addMouseListener(this);

		
		actualPanel=loginPanel;
		actualPanel.setLayout(null);
		actualPanel.setBackground(themedBackground);
		actualPanel.add(lVoltarChooseLogin);
			lVoltarChooseLogin.setIcon(seta);
			lVoltarChooseLogin.setBackground(null);
			lVoltarChooseLogin.setBorder(null);
			lVoltarChooseLogin.setBounds(85,33,42,41);
		actualPanel.add(loginPaneLogo);
			loginPaneLogo.setBounds(576,51,208,180);
		actualPanel.add(loginPaneTitle);
			loginPaneTitle.setBounds(531,267,306,57);
			loginPaneTitle.setOpaque(true);
			loginPaneTitle.setBorder(null);
			loginPaneTitle.setBackground(themedForeground);
			loginPaneTitle.setFont(new Font("SF Pro Display",Font.BOLD,24));
			loginPaneTitle.setForeground(themedBackground);
			loginPaneTitle.setHorizontalAlignment(SwingConstants.CENTER);
			loginPaneTitle.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(lLoginUsername);
			lLoginUsername.setBounds(267,355,386,44);
			lLoginUsername.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lLoginUsername.setForeground(themedForeground);
		actualPanel.add(inLoginUsername);
			inLoginUsername.setBounds(254,390,386,44);
			inLoginUsername.setMargin(new Insets(10,10,10,10));
			inLoginUsername.setBorder(border);
			inLoginUsername.setBackground(themedBackground);
			inLoginUsername.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inLoginUsername.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
		actualPanel.add(lUsernameErrorLog);
			lUsernameErrorLog.setBounds(267,421,386,44);
			lUsernameErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lLoginPassword);
			lLoginPassword.setBounds(742,355,386,44);
			lLoginPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lLoginPassword.setForeground(themedForeground);
		actualPanel.add(inLoginPassword);
			inLoginPassword.setBounds(729,390,386,44);
			inLoginPassword.setMargin(new Insets(10,10,10,10));
			inLoginPassword.setBorder(border);
			inLoginPassword.setBackground(themedBackground);
			inLoginPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			inLoginPassword.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
		actualPanel.add(lPasswordErrorLog);
			lPasswordErrorLog.setBounds(729,421,386,44);
			lPasswordErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lGeneralErrorLog);
			lGeneralErrorLog.setBounds(531,501,306,30);
			lGeneralErrorLog.setForeground(reds[darkRed]);
			lGeneralErrorLog.setHorizontalAlignment(SwingConstants.CENTER);
		actualPanel.add(loginSendText);
			loginSendText.setBounds(531,531,306,57);
			loginSendText.setFont(new Font("SF Pro Display",Font.BOLD,24));
			loginSendText.setForeground(themedBackground);
			loginSendText.setHorizontalAlignment(SwingConstants.CENTER);
			loginSendText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(loginSend);
			loginSend.setFocusable(false);
			loginSend.setBounds(531,531,306,57);
			loginSend.setOpaque(false);
			loginSend.setContentAreaFilled(false);
			loginSend.setBorder(null);
			loginSend.setIcon(smallRoundedButton);
		lVoltarChooseLogin.addActionListener(this);
		lVoltarChooseLogin.addMouseListener(this);
		loginSend.addActionListener(this);
		loginSend.addMouseListener(this);
		
		
		actualPanel=createStoragePanel;
		actualPanel.setLayout(null);
		actualPanel.setBackground(themedBackground);
		actualPanel.add(lStorageName);
			lStorageName.setBounds(267,147,386,44);
			lStorageName.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lStorageName.setForeground(themedForeground);
		actualPanel.add(inStorageName);
			inStorageName.setBounds(254,182,386,44);
			inStorageName.setMargin(new Insets(10,10,10,10));
			inStorageName.setBorder(border);
			inStorageName.setBackground(themedBackground);
			inStorageName.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inStorageName.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
		actualPanel.add(lStorageLength);
			lStorageLength.setBounds(742,147,386,44);
			lStorageLength.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lStorageLength.setForeground(themedForeground);
		actualPanel.add(inStorageLength);
			inStorageLength.setBounds(729,182,386,44);
			inStorageLength.setMargin(new Insets(10,10,10,10));
			inStorageLength.setBorder(border);
			inStorageLength.setBackground(themedBackground);
			inStorageLength.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inStorageLength.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
		actualPanel.add(lStorageWidth);
			lStorageWidth.setBounds(267,236,386,44);
			lStorageWidth.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lStorageWidth.setForeground(themedForeground);
		actualPanel.add(inStorageWidth);
			inStorageWidth.setBounds(254,271,386,44);
			inStorageWidth.setMargin(new Insets(10,10,10,10));
			inStorageWidth.setBorder(border);
			inStorageWidth.setBackground(themedBackground);
			inStorageWidth.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inStorageWidth.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
		actualPanel.add(lNumPisos);
			lNumPisos.setBounds(742,236,386,44);
			lNumPisos.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lNumPisos.setForeground(themedForeground);
		actualPanel.add(inNumPisos);
			inNumPisos.setBounds(729,271,386,44);
			inNumPisos.setMargin(new Insets(10,10,10,10));
			inNumPisos.setBorder(border);
			inNumPisos.setBackground(themedBackground);
			inNumPisos.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inNumPisos.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
		actualPanel.add(lStorageType);
			lStorageType.setBounds(548,395,386,44);
			lStorageType.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			lStorageType.setForeground(themedForeground);
		actualPanel.add(inStorageType);
			inStorageType.setFocusable(false);
			inStorageType.setBounds(531,360,386,44);
			inStorageType.setBorder(border);
			inStorageType.setBackground(themedBackground);
			inStorageType.setFont(new Font("SF Pro Display",Font.BOLD,18));
			inStorageType.setForeground(theme.equals("darkTheme")||theme.equals("darkBlueTheme")?Color.white:null);
			inStorageType.getBackground();
		actualPanel.add(storageSendText);
			storageSendText.setBounds(531,531,306,57);
			storageSendText.setFont(new Font("SF Pro Display",Font.BOLD,24));
			storageSendText.setForeground(themedBackground);
			storageSendText.setHorizontalAlignment(SwingConstants.CENTER);
			storageSendText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(storageSend);
			storageSend.setFocusable(false);
			storageSend.setBounds(531,531,306,57);
			storageSend.setOpaque(false);
			storageSend.setContentAreaFilled(false);
			storageSend.setBorder(null);
			storageSend.setIcon(smallRoundedButton);
		mainFrame.add(titlebarPanel);
		storageSend.addActionListener(this);
		
		actualPanel=mainStoragePanel;
		actualPanel.setLayout(null);
		actualPanel.setBackground(themedBackground);
		actualPanel.add(sidePanel);
			sidePanel.setBounds(0,0,300,711);
			sidePanel.setBorder(BorderFactory.createLineBorder(themedSelected,1));
			sidePanel.setSize(300,711);
			sidePanel.setBackground(themedForeground);
		actualPanel.add(searchBar);
			searchBar.setBounds(600,70,500,40);
			searchBar.setBorder(border);
			searchBar.setBackground(themedBackground);
			searchBar.setFont(new Font("SF Pro Display",Font.PLAIN,18));
			searchBar.setForeground((theme.equals("darkTheme")||theme.equals("darkBlueTheme"))?Color.white:null);
		actualPanel.add(searchButton);
		
		
		c.show(mainPanel,"chooseLoginPanel");
		
		
		mainFrame.setVisible(true);
		

		errorFrame.setSize(500,500);
		errorFrame.setUndecorated(true);
		errorFrame.setLayout(null);
		errorFrame.setIconImage(icon);
		errorFrame.add(errorBoxRemover);
		errorBoxRemover.setBounds(0,0,0,0);
		errorFrame.add(errorTitlebarPanel);
		errorFrame.add(errorPanel);
		errorFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(1,1,1,1,themedForeground));
		
		errorTitlebarPanel.setLayout(null);
		errorTitlebarPanel.setBounds(0,0,500,57);
		errorTitlebarPanel.setBackground(themedForeground);
		errorTitlebarPanel.add(errorLogo);
			errorLogo.setBounds(15,3,50,50);
		errorTitlebarPanel.add(errorClose);
			errorClose.setBounds(443,0,57,57);
			errorClose.setContentAreaFilled(false);
			errorClose.setForeground(themedBackground);
			errorClose.setBorder(null);
		
		errorPanel.setLayout(null);
		errorPanel.setBounds(0,57,500,443);
		errorPanel.setBackground(themedBackground);
		errorPanel.add(errorLogTextArea);
			errorLogTextArea.setBounds(50,20,400,363);
			errorLogTextArea.setBackground(themedBackground);
		errorPanel.add(confirmButton);
			confirmButton.setBounds(220,388,60,40);
			confirmButton.setBorder(null);
			confirmButton.setBackground(themedForeground);
			confirmButton.setFont(new Font("SF Pro Display",Font.BOLD,18));
			confirmButton.setForeground(themedBackground);
		
		errorClose.addActionListener(this);
		errorClose.addMouseListener(this);
		confirmButton.addActionListener(this);
		confirmButton.addMouseListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==close) {
			System.exit(0);
		}
		
		if(e.getSource()==signUp) {
			c.show(mainPanel,"signUpPanel");
		}
		
		if(e.getSource()==signUpSend&&signUpSendSuccessful()) {
			c.show(mainPanel,"chooseLoginPanel");
		}
		
		if(e.getSource()==login) {
			c.show(mainPanel,"loginPanel");
		}
		
		if(e.getSource()==loginSend&&loginSendSuccessful()) {
			if(userHasStorage()) {
				c.show(mainPanel,"mainStoragePanel");
			} else {
				c.show(mainPanel,"createStoragePanel");
			}
		}
		
		if(e.getSource()==lVoltarChooseLogin||e.getSource()==suVoltarChooseLogin) {
			c.show(mainPanel,"chooseLoginPanel");
		}
		
		if(e.getSource()==storageSend&&storageSendSuccessful()) {
			c.show(mainPanel,"mainStoragePanel");
		}
		
		
		
		if(e.getSource()==errorClose) {
			errorFrame.dispose();
		}
		
		if(e.getSource()==confirmButton) {
			errorFrame.dispose();
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e)
	{
		if(e.getSource()==close) {
			close.setContentAreaFilled(true);
			close.setBackground(reds[red]);
		}
		
		if(e.getSource()==login) {
			login.setIcon(darkerBigRoundedButton);
		}
		
		if(e.getSource()==signUp) {
			signUp.setIcon(darkerBigRoundedButton);
		}
		
		if(e.getSource()==suVoltarChooseLogin) {
			suVoltarChooseLogin.setIcon(darkerSeta);
		}
		
		if(e.getSource()==signUpSend) {
			signUpSend.setIcon(darkerSmallRoundedButton);
		}
		
		if(e.getSource()==lVoltarChooseLogin) {
			lVoltarChooseLogin.setIcon(darkerSeta);
		}
		
		if(e.getSource()==loginSend) {
			loginSend.setIcon(darkerSmallRoundedButton);
		}
		
		
		
		if(e.getSource()==errorClose) {
			errorClose.setContentAreaFilled(true);
			errorClose.setBackground(reds[red]);
		}
		
		if(e.getSource()==confirmButton) {
			confirmButton.setBackground(themedSelected);
		}
	}
	
	public void mouseExited(MouseEvent e)
	{
		if(e.getSource()==close) {
			close.setContentAreaFilled(false);
		}
		
		if(e.getSource()==login) {
			login.setIcon(bigRoundedButton);
		}
		
		if(e.getSource()==signUp) {
			signUp.setIcon(bigRoundedButton);
		}
		
		if(e.getSource()==suVoltarChooseLogin) {
			suVoltarChooseLogin.setIcon(seta);
		}
		
		if(e.getSource()==signUpSend) {
			signUpSend.setIcon(smallRoundedButton);
		}
		
		if(e.getSource()==lVoltarChooseLogin) {
			lVoltarChooseLogin.setIcon(seta);
		}
		
		if(e.getSource()==loginSend) {
			loginSend.setIcon(smallRoundedButton);
		}
		
		
		
		if(e.getSource()==errorClose) {
			errorClose.setContentAreaFilled(false);
		}
		
		if(e.getSource()==confirmButton) {
			confirmButton.setBackground(themedForeground);
		}
	}

	
	public static void main(String[] args) {
		new MainClass();
	}
}