//pushed project
package storage;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
public class MainClass implements ActionListener,MouseListener
{	
	//Portatil
	//String connectionString="jdbc:sqlserver://LAPTOP-J55A2J1I\\SQLEXPRESS;database=storage;trustServerCertificate=true;user=sa;password=CPtis2024";
	
	//Fixo
	String connectionString="jdbc:sqlserver://DESKTOP-C774PUO;database=storage;trustServerCertificate=true;user=sa;password=CPtis2024";
	
	public boolean loginSendSuccessful()
	{
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
	
	public boolean checkLoginFields()
	{
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
	
	public boolean signUpSendSuccessful()
	{
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
	
	public boolean userExists()
	{
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
	
	public boolean checkSignUpFields()
	{
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
	CardLayout c=new CardLayout();
	
	Color[] purpleTheme={new Color(222,215,224),new Color(108,91,120),new Color(88,71,100)};
	
	Color[] lightTheme={new Color(242,242,242),new Color(127,127,127),new Color(107,107,107)};
	
	Color[] lightBlueTheme={new Color(96,150,186),new Color(39,76,119),new Color(9,56,99)};
	
	Color[] darkBlueTheme={new Color(13,27,42),new Color(65,90,119),new Color(119,141,169)};
	
	Color[] greenTheme={new Color(173,177,138),new Color(58,90,64),new Color(52,78,65)};
	
	Color[] darkTheme={new Color(18,20,32),new Color(44,43,60),new Color(64,63,76)};
	
	Color[] reds={new Color(200,30,30),new Color(175,0,0)};
	
	int background=0;
	int foreground=1;
	int selected=2;
	
	int red=0;
	int darkRed=1;
	
	String theme="darkTheme";
	
	
	JFrame mainFrame=new JFrame("Storage");
		JButton boxRemover=new JButton();
	JPanel mainPanel=new JPanel();
	
		JPanel titlebarPanel=new JPanel();
			JLabel logo=new JLabel(new ImageIcon("STORAGE Frame Icon tiny.png"));
			JButton close=new JButton(new ImageIcon("closeIcon colored transparent.png"));
			JButton bConta=new JButton();
		
		JPanel chooseLoginPanel=new JPanel();
			JLabel chooseLoginPaneLogo=new JLabel(new ImageIcon((theme.equals("lightTheme")?"MT STORAGE light.png":(theme.equals("darkTheme")?"MT STORAGE dark.png":(theme.equals("darkBlueTheme")?"MT STORAGE darkBlue.png":(theme.equals("lightBlueTheme")?"MT STORAGE lightBlue.png":(theme.equals("greenTheme")?"MT STORAGE green.png":"MT STORAGE.png")))))));
			JButton signUp=new JButton();
			JLabel signUpText=new JLabel("Criar conta");
			JButton login=new JButton();
			JLabel loginText=new JLabel("Login");
			
		JPanel signUpPanel=new JPanel();
			Border line=new LineBorder((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))),2);
			Border empty=new EmptyBorder(0, 10, 0, 0);
			CompoundBorder border = new CompoundBorder(line, empty);
			JButton suVoltarChooseLogin=new JButton();
			JLabel signUpPaneLogo=new JLabel(new ImageIcon((theme.equals("lightTheme")?"MT STORAGE signUp light.png":(theme.equals("darkTheme")?"MT STORAGE signUp dark.png":(theme.equals("darkBlueTheme")?"MT STORAGE signUp darkBlue.png":(theme.equals("lightBlueTheme")?"MT STORAGE signUp lightBlue.png":(theme.equals("greenTheme")?"MT STORAGE signUp green.png":"MT STORAGE signUp.png")))))));
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
			JLabel loginPaneLogo=new JLabel(new ImageIcon((theme.equals("lightTheme")?"MT STORAGE signUp light.png":(theme.equals("darkTheme")?"MT STORAGE signUp dark.png":(theme.equals("darkBlueTheme")?"MT STORAGE signUp darkBlue.png":(theme.equals("lightBlueTheme")?"MT STORAGE signUp lightBlue.png":(theme.equals("greenTheme")?"MT STORAGE signUp green.png":"MT STORAGE signUp.png")))))));
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
		
		JPanel mainAppPanel=new JPanel();
			JTextField searchBar=new JTextField();
			JButton searchButton=new JButton();
		
		JPanel createStoragePanel=new JPanel();
			JLabel lStorageName=new JLabel("Nome da Storage");
			JTextField inStorageName=new JTextField(15);
			JLabel lStorageSize=new JLabel("Tamanho da Storage");
			String[] sizes={"Pequeno","Médio","Grande"};
			JComboBox inStorageSize=new JComboBox(sizes);
			JButton storageSend=new JButton("Criar Storage");
			
	
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
		mainFrame.add(boxRemover);
		boxRemover.setBounds(0,0,0,0);
		mainFrame.add(mainPanel);

		titlebarPanel.setLayout(null);
		titlebarPanel.setBounds(0,0,1366,57);
		titlebarPanel.setBackground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		titlebarPanel.add(logo);
		logo.setBounds(15,3,50,50);
		bConta.setIcon(new ImageIcon("account icon 50.png"));
		bConta.setBounds(1250,0,57,57);
		bConta.setOpaque(false);
		bConta.setContentAreaFilled(false);
		bConta.setBorder(null);
		titlebarPanel.add(close);
		close.setBounds(1309,0,57,57);
		close.setContentAreaFilled(false);
		close.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		close.setBorder(null);
		close.addActionListener(this);
		close.addMouseListener(this);
		bConta.addActionListener(this);
		bConta.addMouseListener(this);
		titlebarPanel.setVisible(true);
		
		mainPanel.setLayout(c);
		mainPanel.setBounds(0,57,1366,711);
		mainPanel.add("chooseLoginPanel",chooseLoginPanel);
		mainPanel.add("signUpPanel",signUpPanel);
		mainPanel.add("loginPanel",loginPanel);
		mainPanel.add("mainAppPanel",mainAppPanel);
		mainPanel.add("createStoragePanel",createStoragePanel);
		mainPanel.setVisible(true);
		
		actualPanel=chooseLoginPanel;
		actualPanel.setBounds(0,57,1366,711);
		actualPanel.setLayout(null);
		actualPanel.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		actualPanel.add(chooseLoginPaneLogo);
		chooseLoginPaneLogo.setBounds(504,77,355,309);
		actualPanel.add(signUpText);
		signUpText.setBounds(502,453,361,57);
		signUpText.setFont(new Font("SF Pro Display",Font.BOLD,24));
		signUpText.setHorizontalAlignment(SwingConstants.CENTER);
		signUpText.setVerticalAlignment(SwingConstants.CENTER);
		signUpText.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		actualPanel.add(signUp);
		signUp.setBounds(502,453,361,57);
		signUp.setContentAreaFilled(false);
		signUp.setOpaque(false);
		signUp.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button 2 light.png":(theme.equals("darkTheme")?"rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"rounded button 2 green.png":"rounded button 2.png"))))));
		signUp.setBorder(null);
		actualPanel.add(loginText);
		loginText.setBounds(502,538,361,57);
		loginText.setFont(new Font("SF Pro Display",Font.BOLD,24));
		loginText.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		loginText.setHorizontalAlignment(SwingConstants.CENTER);
		loginText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(login);
		login.setBounds(502,538,361,57);
		login.setBorder(null);
		login.setContentAreaFilled(false);
		login.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button 2 light.png":(theme.equals("darkTheme")?"rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"rounded button 2 green.png":"rounded button 2.png"))))));
		signUp.addActionListener(this);
		signUp.addMouseListener(this);
		login.addActionListener(this);
		login.addMouseListener(this);
		
		actualPanel=signUpPanel;
		actualPanel.setLayout(null);
		actualPanel.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		actualPanel.add(suVoltarChooseLogin);
		suVoltarChooseLogin.setIcon(new ImageIcon((theme.equals("lightTheme")?"seta light.png":(theme.equals("darkTheme")?"seta dark.png":(theme.equals("darkBlueTheme")?"seta darkBlue.png":(theme.equals("lightBlueTheme")?"seta lightBlue.png":(theme.equals("greenTheme")?"seta green.png":"seta.png")))))));
		suVoltarChooseLogin.setBackground(null);
		suVoltarChooseLogin.setBorder(null);
		suVoltarChooseLogin.setBounds(83,33,42,41);
		actualPanel.add(signUpPaneLogo);
		signUpPaneLogo.setBounds(576,51,208,180);
		actualPanel.add(signUpPaneTitle);
		signUpPaneTitle.setBounds(531,267,306,57);
		signUpPaneTitle.setOpaque(true);
		signUpPaneTitle.setBorder(null);
		signUpPaneTitle.setBackground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		signUpPaneTitle.setFont(new Font("SF Pro Display",Font.BOLD,24));
		signUpPaneTitle.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		signUpPaneTitle.setHorizontalAlignment(SwingConstants.CENTER);
		signUpPaneTitle.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(lSignUpUsername);
		lSignUpUsername.setBounds(267,337,386,44);
		lSignUpUsername.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		lSignUpUsername.setForeground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		actualPanel.add(inSignUpUsername);
		inSignUpUsername.setBounds(254,372,386,44);
		inSignUpUsername.setMargin(new Insets(10,10,10,10));
		inSignUpUsername.setBorder(border);
		inSignUpUsername.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		inSignUpUsername.setFont(new Font("SF Pro Display",Font.BOLD,18));
		actualPanel.add(suUsernameErrorLog);
		suUsernameErrorLog.setBounds(267,403,386,44);
		suUsernameErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lSignUpEmail);
		lSignUpEmail.setBounds(267,426,386,44);
		lSignUpEmail.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		lSignUpEmail.setForeground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		actualPanel.add(inSignUpEmail);
		inSignUpEmail.setBounds(254,461,386,44);
		inSignUpEmail.setMargin(new Insets(10,10,10,10));
		inSignUpEmail.setBorder(border);
		inSignUpEmail.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		inSignUpEmail.setFont(new Font("SF Pro Display",Font.BOLD,18));
		actualPanel.add(suEmailErrorLog);
		suEmailErrorLog.setBounds(267,492,386,44);
		suEmailErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lSignUpPhone);
		lSignUpPhone.setBounds(742,337,386,44);
		lSignUpPhone.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		lSignUpPhone.setForeground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		actualPanel.add(inSignUpPhone);
		inSignUpPhone.setBounds(729,372,386,44);
		inSignUpPhone.setMargin(new Insets(10,10,10,10));
		inSignUpPhone.setBorder(border);
		inSignUpPhone.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		inSignUpPhone.setFont(new Font("SF Pro Display",Font.BOLD,18));
		actualPanel.add(suPhoneErrorLog);
		suPhoneErrorLog.setBounds(742,403,386,44);
		suPhoneErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lSignUpPassword);
		lSignUpPassword.setBounds(742,426,386,44);
		lSignUpPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		lSignUpPassword.setForeground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		actualPanel.add(inSignUpPassword);
		inSignUpPassword.setBounds(729,461,386,44);
		inSignUpPassword.setMargin(new Insets(10,10,10,10));
		inSignUpPassword.setBorder(border);
		inSignUpPassword.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		inSignUpPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		actualPanel.add(suPasswordErrorLog);
		suPasswordErrorLog.setBounds(742,492,386,44);
		suPasswordErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(signUpSendText);
		signUpSendText.setBounds(531,551,306,57);
		signUpSendText.setFont(new Font("SF Pro Display",Font.BOLD,24));
		signUpSendText.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		signUpSendText.setHorizontalAlignment(SwingConstants.CENTER);
		signUpSendText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(signUpSend);
		signUpSend.setBounds(531,551,306,57);
		signUpSend.setOpaque(false);
		signUpSend.setContentAreaFilled(false);
		signUpSend.setBorder(null);
		signUpSend.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button light.png":(theme.equals("darkTheme")?"rounded button dark.png":(theme.equals("darkBlueTheme")?"rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button lightBlue.png":(theme.equals("greenTheme")?"rounded button green.png":"rounded button.png"))))));
		suVoltarChooseLogin.addActionListener(this);
		suVoltarChooseLogin.addMouseListener(this);
		signUpSend.addActionListener(this);
		signUpSend.addMouseListener(this);

		actualPanel=loginPanel;
		actualPanel.setLayout(null);
		actualPanel.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		actualPanel.add(lVoltarChooseLogin);
		lVoltarChooseLogin.setIcon(new ImageIcon((theme.equals("lightTheme")?"seta light.png":(theme.equals("darkTheme")?"seta dark.png":(theme.equals("darkBlueTheme")?"seta darkBlue.png":(theme.equals("lightBlueTheme")?"seta lightBlue.png":(theme.equals("greenTheme")?"seta green.png":"seta.png")))))));
		lVoltarChooseLogin.setBackground(null);
		lVoltarChooseLogin.setBorder(null);
		lVoltarChooseLogin.setBounds(85,33,42,41);
		actualPanel.add(loginPaneLogo);
		loginPaneLogo.setBounds(576,51,208,180);
		actualPanel.add(loginPaneTitle);
		loginPaneTitle.setBounds(531,267,306,57);
		loginPaneTitle.setOpaque(true);
		loginPaneTitle.setBorder(null);
		loginPaneTitle.setBackground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		loginPaneTitle.setFont(new Font("SF Pro Display",Font.BOLD,24));
		loginPaneTitle.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		loginPaneTitle.setHorizontalAlignment(SwingConstants.CENTER);
		loginPaneTitle.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(lLoginUsername);
		lLoginUsername.setBounds(267,355,386,44);
		lLoginUsername.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		lLoginUsername.setForeground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		actualPanel.add(inLoginUsername);
		inLoginUsername.setBounds(254,390,386,44);
		inLoginUsername.setMargin(new Insets(10,10,10,10));
		inLoginUsername.setBorder(border);
		inLoginUsername.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		inLoginUsername.setFont(new Font("SF Pro Display",Font.BOLD,18));
		actualPanel.add(lUsernameErrorLog);
		lUsernameErrorLog.setBounds(267,421,386,44);
		lUsernameErrorLog.setForeground(reds[darkRed]);
		actualPanel.add(lLoginPassword);
		lLoginPassword.setBounds(742,355,386,44);
		lLoginPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
		lLoginPassword.setForeground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		actualPanel.add(inLoginPassword);
		inLoginPassword.setBounds(729,390,386,44);
		inLoginPassword.setMargin(new Insets(10,10,10,10));
		inLoginPassword.setBorder(border);
		inLoginPassword.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		inLoginPassword.setFont(new Font("SF Pro Display",Font.PLAIN,18));
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
		loginSendText.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		loginSendText.setHorizontalAlignment(SwingConstants.CENTER);
		loginSendText.setVerticalAlignment(SwingConstants.CENTER);
		actualPanel.add(loginSend);
		loginSend.setBounds(531,531,306,57);
		loginSend.setOpaque(false);
		loginSend.setContentAreaFilled(false);
		loginSend.setBorder(null);
		loginSend.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button light.png":(theme.equals("darkTheme")?"rounded button dark.png":(theme.equals("darkBlueTheme")?"rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button lightBlue.png":(theme.equals("greenTheme")?"rounded button green.png":"rounded button.png"))))));
		lVoltarChooseLogin.addActionListener(this);
		lVoltarChooseLogin.addMouseListener(this);
		loginSend.addActionListener(this);
		loginSend.addMouseListener(this);
		
		actualPanel=mainAppPanel;
		
		actualPanel=createStoragePanel;
		actualPanel.setLayout(new FlowLayout());
		actualPanel.add(lStorageName);
		actualPanel.add(inStorageName);
		actualPanel.add(lStorageSize);
		actualPanel.add(inStorageSize);
		mainFrame.add(titlebarPanel);
		
		chooseLogin();
		
		
		mainFrame.setVisible(true);
		

		errorFrame.setSize(500,500);
		errorFrame.setUndecorated(true);
		errorFrame.setLayout(null);
		errorFrame.setIconImage(icon);
		errorFrame.add(errorBoxRemover);
		errorBoxRemover.setBounds(0,0,0,0);
		errorFrame.add(errorTitlebarPanel);
		errorFrame.add(errorPanel);
		errorFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(1,1,1,1,(theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground])))))));
		
		errorTitlebarPanel.setLayout(null);
		errorTitlebarPanel.setBounds(0,0,500,57);
		errorTitlebarPanel.setBackground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		errorTitlebarPanel.add(errorLogo);
		errorLogo.setBounds(15,3,50,50);
		errorTitlebarPanel.add(errorClose);
		errorClose.setBounds(443,0,57,57);
		errorClose.setContentAreaFilled(false);
		errorClose.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		errorClose.setBorder(null);
		
		errorPanel.setLayout(null);
		errorPanel.setBounds(0,57,500,443);
		errorPanel.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		errorPanel.add(errorLogTextArea);
		errorLogTextArea.setBounds(50,20,400,363);
		errorLogTextArea.setBackground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
		errorPanel.add(confirmButton);
		confirmButton.setBounds(220,388,60,40);
		confirmButton.setBorder(null);
		confirmButton.setBackground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		confirmButton.setFont(new Font("SF Pro Display",Font.BOLD,18));
		confirmButton.setForeground((theme.equals("lightTheme")?lightTheme[background]:(theme.equals("darkTheme")?darkTheme[background]:(theme.equals("darkBlueTheme")?darkBlueTheme[background]:(theme.equals("lightBlueTheme")?lightBlueTheme[background]:(theme.equals("greenTheme")?greenTheme[background]:purpleTheme[background]))))));
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
			signUp();				
		}
		
		if(e.getSource()==signUpSend&&signUpSendSuccessful()) {
			chooseLogin();
		}
		
		if(e.getSource()==login) {
			login();
		}
		
		if(e.getSource()==loginSend&&loginSendSuccessful()) {
			titlebarPanel.add(bConta);
			createStorage();			
		}
		
		if(e.getSource()==lVoltarChooseLogin||e.getSource()==suVoltarChooseLogin) {
			chooseLogin();
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
			login.setIcon(new ImageIcon(theme.equals("lightTheme")?"darker rounded button 2 light.png":(theme.equals("darkTheme")?"darker rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"darker rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"darker rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"darker rounded button 2 green.png":"darker rounded button 2.png"))))));
		}
		
		if(e.getSource()==signUp) {
			signUp.setIcon(new ImageIcon(theme.equals("lightTheme")?"darker rounded button 2 light.png":(theme.equals("darkTheme")?"darker rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"darker rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"darker rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"darker rounded button 2 green.png":"darker rounded button 2.png"))))));
		}
		
		if(e.getSource()==suVoltarChooseLogin) {
			suVoltarChooseLogin.setIcon(new ImageIcon((theme.equals("lightTheme")?"seta escura light.png":(theme.equals("darkTheme")?"seta escura dark.png":(theme.equals("darkBlueTheme")?"seta escura darkBlue.png":(theme.equals("lightBlueTheme")?"seta escura lightBlue.png":(theme.equals("greenTheme")?"seta escura green.png":"seta escura.png")))))));
		}
		
		if(e.getSource()==signUpSend) {
			signUpSend.setIcon(new ImageIcon(theme.equals("lightTheme")?"darker rounded button light.png":(theme.equals("darkTheme")?"darker rounded button dark.png":(theme.equals("darkBlueTheme")?"darker rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"darker rounded button lightBlue.png":(theme.equals("greenTheme")?"darker rounded button green.png":"darker rounded button.png"))))));
		}
		
		if(e.getSource()==lVoltarChooseLogin) {
			lVoltarChooseLogin.setIcon(new ImageIcon((theme.equals("lightTheme")?"seta escura light.png":(theme.equals("darkTheme")?"seta escura dark.png":(theme.equals("darkBlueTheme")?"seta escura darkBlue.png":(theme.equals("lightBlueTheme")?"seta escura lightBlue.png":(theme.equals("greenTheme")?"seta escura green.png":"seta escura.png")))))));
		}
		
		if(e.getSource()==loginSend) {
			loginSend.setIcon(new ImageIcon(theme.equals("lightTheme")?"darker rounded button light.png":(theme.equals("darkTheme")?"darker rounded button dark.png":(theme.equals("darkBlueTheme")?"darker rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"darker rounded button lightBlue.png":(theme.equals("greenTheme")?"darker rounded button green.png":"darker rounded button.png"))))));
		}
		
		
		
		if(e.getSource()==errorClose) {
			errorClose.setContentAreaFilled(true);
			errorClose.setBackground(reds[red]);
		}
		
		if(e.getSource()==confirmButton) {
			confirmButton.setBackground((theme.equals("lightTheme")?lightTheme[selected]:(theme.equals("darkTheme")?darkTheme[selected]:(theme.equals("darkBlueTheme")?darkBlueTheme[selected]:(theme.equals("lightBlueTheme")?lightBlueTheme[selected]:(theme.equals("greenTheme")?greenTheme[selected]:purpleTheme[selected]))))));
		}
	}
	
	public void mouseExited(MouseEvent e)
	{
		if(e.getSource()==close) {
			close.setContentAreaFilled(false);
		}
		
		if(e.getSource()==login) {
			login.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button 2 light.png":(theme.equals("darkTheme")?"rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"rounded button 2 green.png":"rounded button 2.png"))))));
		}
		
		if(e.getSource()==signUp) {
			signUp.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button 2 light.png":(theme.equals("darkTheme")?"rounded button 2 dark.png":(theme.equals("darkBlueTheme")?"rounded button 2 darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button 2 lightBlue.png":(theme.equals("greenTheme")?"rounded button 2 green.png":"rounded button 2.png"))))));
		}
		
		if(e.getSource()==suVoltarChooseLogin) {
			suVoltarChooseLogin.setIcon(new ImageIcon((theme.equals("lightTheme")?"seta light.png":(theme.equals("darkTheme")?"seta dark.png":(theme.equals("darkBlueTheme")?"seta darkBlue.png":(theme.equals("lightBlueTheme")?"seta lightBlue.png":(theme.equals("greenTheme")?"seta green.png":"seta.png")))))));
		}
		
		if(e.getSource()==signUpSend) {
			signUpSend.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button light.png":(theme.equals("darkTheme")?"rounded button dark.png":(theme.equals("darkBlueTheme")?"rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button lightBlue.png":(theme.equals("greenTheme")?"rounded button green.png":"rounded button.png"))))));
		}
		
		if(e.getSource()==lVoltarChooseLogin) {
			lVoltarChooseLogin.setIcon(new ImageIcon((theme.equals("lightTheme")?"seta light.png":(theme.equals("darkTheme")?"seta dark.png":(theme.equals("darkBlueTheme")?"seta darkBlue.png":(theme.equals("lightBlueTheme")?"seta lightBlue.png":(theme.equals("greenTheme")?"seta green.png":"seta.png")))))));
		}
		
		if(e.getSource()==loginSend) {
			loginSend.setIcon(new ImageIcon(theme.equals("lightTheme")?"rounded button light.png":(theme.equals("darkTheme")?"rounded button dark.png":(theme.equals("darkBlueTheme")?"rounded button darkBlue.png":(theme.equals("lightBlueTheme")?"rounded button lightBlue.png":(theme.equals("greenTheme")?"rounded button green.png":"rounded button.png"))))));
		}
		
		
		
		if(e.getSource()==errorClose) {
			errorClose.setContentAreaFilled(false);
		}
		
		if(e.getSource()==confirmButton) {
			confirmButton.setBackground((theme.equals("lightTheme")?lightTheme[foreground]:(theme.equals("darkTheme")?darkTheme[foreground]:(theme.equals("darkBlueTheme")?darkBlueTheme[foreground]:(theme.equals("lightBlueTheme")?lightBlueTheme[foreground]:(theme.equals("greenTheme")?greenTheme[foreground]:purpleTheme[foreground]))))));
		}
	}
	
	
	
	public static void main(String[] args)
	{
		new MainClass();
	}
	
	public void chooseLogin()
	{
		c.show(mainPanel,"chooseLoginPanel");
	}
	
	public void signUp()
	{
		c.show(mainPanel,"signUpPanel");
	}
	
	public void login()
	{
		c.show(mainPanel,"loginPanel");
	}
	
	public void createStorage()
	{
		c.show(mainPanel,"createStoragePanel");
	}
	
	public void mainApp()
	{
		c.show(mainPanel,"mainAppPanel");
	}
}