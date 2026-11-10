package com.example.muzyka;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static class Album {
        String wykonawca;
        String tytul;
        int liczbaUtworow;
        int rokWydania;
        long liczbaPobran;

        public Album(String wykonawca, String tytul, int liczbaUtworow, int rokWydania, long liczbaPobran) {
            this.wykonawca = wykonawca;
            this.tytul = tytul;
            this.liczbaUtworow = liczbaUtworow;
            this.rokWydania = rokWydania;
            this.liczbaPobran = liczbaPobran;
        }
    }

    private TextView tvWykonawca, tvTytul, tvUtwory, tvRok, tvPobrania;
    private Button btnPobierz;
    private ImageButton btnLewo, btnPrawo;

    private List<Album> listaAlbumy = new ArrayList<>();
    private int aktualnyIndeks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWykonawca = findViewById(R.id.wykonawcy);
        tvTytul = findViewById(R.id.album);
        tvUtwory = findViewById(R.id.utwory);
        tvRok = findViewById(R.id.rok);
        tvPobrania = findViewById(R.id.pobrania);
        btnPobierz = findViewById(R.id.pobierz);
        btnLewo = findViewById(R.id.lewo);
        btnPrawo = findViewById(R.id.prawo);

        wczytajDaneZPlikuAssets();

        wyswietlAktualnyAlbum();

        btnLewo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaAlbumy.isEmpty()) return;

                aktualnyIndeks--;
                if (aktualnyIndeks < 0) {
                    aktualnyIndeks = listaAlbumy.size() - 1;
                }
                wyswietlAktualnyAlbum();
            }
        });

        btnPrawo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaAlbumy.isEmpty()) return;

                aktualnyIndeks++;
                if (aktualnyIndeks >= listaAlbumy.size()) {
                    aktualnyIndeks = 0;
                }
                wyswietlAktualnyAlbum();
            }
        });

        btnPobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaAlbumy.isEmpty()) return;

                listaAlbumy.get(aktualnyIndeks).liczbaPobran++;

                wyswietlAktualnyAlbum();
            }
        });
    }

    private void wyswietlAktualnyAlbum() {
        if (listaAlbumy.isEmpty()) return;

        Album album = listaAlbumy.get(aktualnyIndeks);
        tvWykonawca.setText(album.wykonawca);
        tvTytul.setText(album.tytul);
        tvUtwory.setText(album.liczbaUtworow + " utworów");
        tvRok.setText(String.valueOf(album.rokWydania));
        tvPobrania.setText(String.valueOf(album.liczbaPobran));
    }

    private void wczytajDaneZPlikuAssets() {
        BufferedReader reader = null;
        try {
            // Otwieramy plik data.txt z folderu assets linijka po linijce
            reader = new BufferedReader(new InputStreamReader(getAssets().open("Data.txt")));
            String linia;

            while ((linia = reader.readLine()) != null) {
                // Usuwamy ewentualne białe znaki (np. spacje na końcu)
                linia = linia.trim();

                // Jeśli linia jest pusta (odstęp między albumami), pomijamy ją
                if (linia.isEmpty()) {
                    continue;
                }

                // Skoro linia nie była pusta, to jest to 'wykonawca'
                String wykonawca = linia;

                // Czytamy kolejne linie przypisane do danego albumu
                String tytul = reader.readLine().trim();
                int liczbaUtworow = Integer.parseInt(reader.readLine().trim());
                int rokWydania = Integer.parseInt(reader.readLine().trim());
                long liczbaPobran = Long.parseLong(reader.readLine().trim());

                // Tworzymy obiekt Album i dodajemy go do listy
                listaAlbumy.add(new Album(wykonawca, tytul, liczbaUtworow, rokWydania, liczbaPobran));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd podczas wczytywania pliku danych!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd formatu danych w pliku!", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
