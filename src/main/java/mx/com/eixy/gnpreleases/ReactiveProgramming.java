package mx.com.eixy.gnpreleases;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;

import io.reactivex.*;
import io.reactivex.functions.Function;

import org.junit.Test;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

public class ReactiveProgramming {

	public static void main(String[] args) throws Exception {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame jf = new JFrame();
				JButton jb = new JButton("Hola");
				jf.add(jb);
				jf.pack();
				jf.setVisible(true);

				/*
				create(jb).debounce(2, TimeUnit.SECONDS).distinctUntilChanged().switchMap(new Function<String, ObservableSource<String>>() {
                    public ObservableSource<String> apply(String query) throws Exception {
                        return busca(query);
                    }
                }).subscribe(onNext -> {
					System.out.println("onNext " + onNext);
				});*/
				
				create(jb).debounce(2, TimeUnit.SECONDS).distinctUntilChanged().switchMap(term-> busca(term)).subscribeOn(Schedulers.io()).subscribe(onNext -> {
					EventQueue.invokeLater(()-> {
					System.out.println("onNext2 " + onNext + " " +Thread.currentThread());
					});
				});
				
				Schedulers.computation();
				
			}
		});
	}
	
	private static void hola(JButton jbutton) {
		Observable.create(emitter->jbutton.addActionListener(event-> emitter.onNext(event)
		));
	}
	
	private static Observable busca(Object term) {
		System.out.println("Buscando en..." + Thread.currentThread());
		Observable observable = Observable.fromIterable(Arrays.asList("foo", "bar","ces"));		
		return observable;
	}

	
	private static Observable create(JButton button) {
		return Observable.create(e -> button.addActionListener(event -> {
			e.onNext(event.toString());
		}));
	}

}
