import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Breakout extends GraphicsProgram {

	int PEN_EN, PEN_BOY, tugla_en, tugla_boy;
	int sutun_sayisi = 15, satir_sayisi=10;
	GRect raket;
	GOval top;
	int raket_en = 150, raket_boy = 20, CAP=15;
	int hiz_x = 5, hiz_y = -5;//topun x ve y hizlari
	RandomGenerator rg = RandomGenerator.getInstance();
	int sayac=0, can=3;//Sayac kac adet tugla vuruldugunu 
	GLabel can_metni, skor_metni;
	GLabel tekrar, cikis;
		
	public void run() {

		addMouseListeners();
		setSize(1080,720);
		setBackground(Color.BLACK);
		PEN_EN = getWidth();
		PEN_BOY = getHeight();

		oyun();

	}

	private void oyun() {
		hiz_x = 5;
		hiz_y = -5;
		sayac=0;
		can=3;
		removeAll();
		metinleriEkle();		
		tuglalariDiz();
		dinamikleriYerlestir();
		animasyon();
		bitis();
	}

	private void bitis() {
		removeAll();
		tekrar = new GLabel("Tekrar Oyna");
		tekrar.setFont("Jokerman-20");
		tekrar.setColor(Color.WHITE);
		
		cikis = new GLabel("Oyundan Cik");
		cikis.setFont("Jokerman-20");
		cikis.setColor(Color.WHITE);

		if(can==0) {
			GLabel kaybettin = new GLabel("Kaybettin... Cik Disari...");
			kaybettin.setFont("Jokerman-30");
			kaybettin.setColor(Color.RED);
			add(kaybettin,
					PEN_EN/2-kaybettin.getWidth()/2,
					PEN_BOY/2-kaybettin.getHeight()/2);
			add(tekrar, PEN_EN/2-tekrar.getWidth()*3/2, 
					kaybettin.getY()+tekrar.getHeight()*3/2);
			add(cikis, PEN_EN/2+tekrar.getWidth()*3/2-cikis.getWidth(), 
					kaybettin.getY()+tekrar.getHeight()*3/2);

		}else {
			GLabel kazandin = new GLabel("Tebrikler...");
			kazandin.setFont("Jokerman-30");
			kazandin.setColor(Color.GREEN);
			add(kazandin,
					PEN_EN/2-kazandin.getWidth()/2,
					PEN_BOY/2-kazandin.getHeight()/2);
			add(tekrar, PEN_EN/2-tekrar.getWidth()*3/2, 
					kazandin.getY()+tekrar.getHeight()*3/2);
			add(cikis, PEN_EN/2+tekrar.getWidth()*3/2-cikis.getWidth(), 
					kazandin.getY()+tekrar.getHeight()*3/2);
		}
	}

	private void animasyon() {
		int BEKLEME_SURESI = 10;
		waitForClick();
		while(true) {
			top.move(hiz_x, hiz_y);
			if(top.getX() <= 0 || top.getX()+CAP >= PEN_EN) {
				hiz_x = -hiz_x;
			}
			if(top.getY() <= 50) {
				hiz_y = -hiz_y;
			}//kenar kontrolleri bitisi

			carpisma(top.getX(), top.getY());//sol ust
			//carpisma(top.getX()+CAP, top.getY());//sag ust
			//carpisma(top.getX(), top.getY()+CAP);//sol alt
			//carpisma(top.getX()+CAP, top.getY()+CAP);//sag alt

			if(top.getY() >= raket.getY()+CAP) {
				can--;
				remove(top);
				remove(raket);

				for(int i=0; i<3; i++) {
					setBackground(rg.nextColor());
					pause(300);
					for(int j=0; j<11; j++) {
						setBackground(rg.nextColor());
						pause(25);
					}
				}
				setBackground(Color.BLACK);
				can_metni.setLabel("Can: "+can);
				if(can == 0) {
					can_metni.setColor(Color.RED);
					break;
				}
				dinamikleriYerlestir();
				waitForClick();
			}//oyun kaybi

			if(sayac == satir_sayisi*sutun_sayisi) {
				break;
			}

			pause(BEKLEME_SURESI);
		}//animasyon bitisi
	}

	private void carpisma(double x, double y) {
		GObject obje = getElementAt(x, y);
		if(obje != null) {
			hiz_y *= -1;
			if(obje != raket && obje.getClass() != can_metni.getClass()) {
				remove(obje);
				sayac++;
				skor_metni.setLabel("Skor: "+sayac);
			}
			if(obje == raket) {
				hiz_x = rg.nextInt(-5,5);
			}
		}
	}

	private void dinamikleriYerlestir() {

		raket = new GRect(raket_en, raket_boy);
		raket.setFilled(true);
		raket.setFillColor(Color.GRAY);
		add(raket, PEN_EN/2-(raket_en/2), PEN_BOY-50);

		top = new GOval(CAP, CAP);
		top.setFilled(true);
		top.setFillColor(Color.RED);
		add(top, PEN_EN/2-(CAP/2), raket.getY()-CAP);
	}

	private void tuglalariDiz() {

		tugla_en = PEN_EN/sutun_sayisi;
		tugla_boy = PEN_BOY/(satir_sayisi*2);

		for(int sutun=0; sutun<sutun_sayisi; sutun++) {
			for(int satir=0; satir<satir_sayisi; satir++) {
				GRect tugla = new GRect(tugla_en, tugla_boy);
				tugla.setFilled(true);
				tugla.setFillColor(Color.BLUE);
				add(tugla, sutun*tugla_en, satir*tugla_boy+50);
			}
		}
	}

	private void metinleriEkle() {
		can_metni = new GLabel("Can: "+can);
		can_metni.setFont("Jokerman-30");
		can_metni.setColor(Color.GREEN);
		add(can_metni,10,can_metni.getHeight());

		skor_metni = new GLabel("Skor: "+sayac);
		skor_metni.setFont("Jokerman-30");
		skor_metni.setColor(Color.GREEN);
		add(skor_metni,PEN_EN-skor_metni.getWidth(),skor_metni.getHeight());
	}


	public void mouseMoved(MouseEvent fare) {
		int x = fare.getX()-raket_en/2;
		raket.setLocation(x, raket.getY());
	}

	public void mouseClicked(MouseEvent fare) {

		GObject obje = getElementAt(fare.getX(), fare.getY());
		if(obje == cikis) {
			System.exit(0);
		}		
	}

	
	
	
}










