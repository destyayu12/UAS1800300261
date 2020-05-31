package com.aaa183.meliawati;

import android.content.ContentValues;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_bukuku";
    private final static String TABLE_BUKU = "t_buku";
    private final static String KEY_ID_BUKU = "ID_Buku";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_CAPTION = "Caption";
    private final static String KEY_PENULIS = "Penulis";
    private final static String KEY_ISI_BUKU = "Isi_Buku";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BUKU = "CREATE TABLE " + TABLE_BUKU
                + "(" + KEY_ID_BUKU + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_CAPTION + " TEXT, "
                + KEY_PENULIS + " TEXT, " + KEY_ISI_BUKU + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BUKU);
        inialisasiBukuAwal(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BUKU;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void tambahBuku(Buku dataBuku) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_CAPTION, dataBuku.getCaption());
        cv.put(KEY_PENULIS, dataBuku.getCaption());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.insert(TABLE_BUKU, null, cv);
        db.close();
    }

    public void tambahBuku(Buku dataBuku, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_CAPTION, dataBuku.getCaption());
        cv.put(KEY_PENULIS, dataBuku.getCaption());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.insert(TABLE_BUKU, null, cv);
    }

    public void editBuku(Buku dataBuku) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_CAPTION, dataBuku.getCaption());
        cv.put(KEY_PENULIS, dataBuku.getCaption());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.update(TABLE_BUKU, cv, KEY_ID_BUKU + "=?", new String[]{String.valueOf(dataBuku.getIdBuku())});
        db.close();
    }

    public void hapusBuku(int idBuku) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BUKU, KEY_ID_BUKU + "=?", new String[]{String.valueOf(idBuku)});
        db.close();
    }

    public ArrayList<Buku> getAllBuku() {
        ArrayList<Buku> dataBuku = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BUKU;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()) {
            do {
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Buku tempBuku = new Buku(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataBuku.add(tempBuku);
            } while (csr.moveToNext());
        }
        return dataBuku;
    }

    private String storeImagesFiles(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inialisasiBukuAwal(SQLiteDatabase db) {
        int idBuku = 0;
        Date tempDate = new Date();

        try {
            tempDate = sdFormat.parse("13/03/2020 06:22");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku1 = new Buku(
                idBuku,
                "Can't Take My Eyes Off You",
                tempDate,
                storeImagesFiles(R.drawable.buku1),
                "Fantasy Romance",
                "Qing Feng Mo Wan",
                "\"Komandan Lu, ada desas-desus bahwa kita tidur bersama, kita berselingkuh!\"\n" +
                        "Dia mendongak dari bawah selimut dan perlahan bangkit. \"Apa yang kamu mau dari aku?\"\n" +
                        "\"Tolak, katakan itu palsu, hentikan penyebaran ...\"\n" +
                        "Dia mendorongnya ke dinding, menutup mulutnya dengan ciuman kasar. \"Nyonya. Lu, kita harus membuktikan rumor itu. ”\n" +
                        "Sebelum dia dilahirkan kembali, apa yang telah dia lakukan adalah menjauhinya!\n" +
                        "Setelah dia dilahirkan kembali, apa yang selalu ingin dia lakukan adalah memberinya keturunan!\n" +
                        "Lu Xingzhi memperjuangkan dua moto pribadi sepanjang hidupnya:\n" +
                        "Pertama, merusak pernikahan militer adalah ilegal.\n" +
                        "Dua, Jiang Yao adalah miliknya dalam keabadian ini, sampai kematian memisahkan mereka.",
                "https://www.webnovel.com/book/16248899505324405"
        );

        tambahBuku(buku1, db);
        idBuku++;

        try {
            tempDate = sdFormat.parse("13/03/2020 06:22");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku2 = new Buku(
                idBuku,
                "My Youth Began With Him",
                tempDate,
                storeImagesFiles(R.drawable.buku2),
                "Contemporary Romance",
                "Baby Piggie",
                "Tujuh tahun lalu, setelah perpisahan mereka, dia menghilang tanpa jejak.\n" +
                        "Sekarang, dia muncul kembali pada malam pernikahannya, tidak ada alasan untuk memaksanya menikahinya ...\n" +
                        "Dengan sertifikat pernikahan, dia mengikatnya tanpa ampun ke sisinya.\n" +
                        "Dari sana, \"Cinderella\" ini memulai perjalanannya sebagai istri pewaris kerajaan bisnis ...\n" +
                        "Ny. Huo - tenang, berlidah tajam, dan sangat cerdas.\n" +
                        "Mr Qin - spoiler istri tanpa akhir dan \"budak\" lengkap untuk putri mereka.\n" +
                        "Kisah cinta yang berkualitas, satu lawan satu. Anda dipersilakan untuk terpikat pada kisah ini bersama kami.\n" +
                        "Stats on QQ Reader: 102 juta favorit, dan terpilih sebagai 8,4 / 10 oleh 41 ribu pembaca.\n" +
                        "Catatan TL: Mungkin salah satu cerita terbaik yang pernah saya baca, dan saya sangat tergoda untuk menerjemahkan judulnya ke Big D Brings Spring. Para gadis, saya harap Anda menikmatinya sama seperti pacar saya dan penerjemah perempuan saya yang pesta membaca 4000 bab dalam waktu sekitar satu bulan.\n" +
                        "Untuk semua pria yang ragu untuk mencobanya, Anda tidak akan menyesalinya. Ini buku jantan, sumpah.",
                "https://www.webnovel.com/book/10260480905004505"
        );

        tambahBuku(buku2, db);
        idBuku++;

        try {
            tempDate = sdFormat.parse("13/03/2020 06:22");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku3 = new Buku(
                idBuku,
                "The CEO's Woman",
                tempDate,
                storeImagesFiles(R.drawable.buku3),
                "Contemporary Romance",
                "TheBlips",
                "* TANPA RAPE dan Kesalahpahaman besar! *\n" +
                        "\n" +
                        "Jika dia bisa menggunakan tiga kata untuk menggambarkan dirinya, Jiang Yue akan mengatakan bahwa dia kaya, cantik, dan sangat pintar. Sayangnya, dia menerima ini begitu saja dalam kehidupan masa lalunya, menyebabkan kejatuhannya.\n" +
                        "\n" +
                        "Setelah bereinkarnasi ke dirinya yang berusia lima belas tahun, Jiang Yue hanya menginginkan tiga hal: membangun sebuah kerajaan, menghabiskan lebih banyak waktu dengan orang-orang yang mencintainya dan menjalani kehidupan yang bahagia.\n" +
                        "\n" +
                        "...\n" +
                        "\n" +
                        "Dalam kehidupan masa lalunya, dia tidak dapat mengekspresikan cintanya pada Jiang Yue.\n" +
                        "\n" +
                        "Sekarang dia telah bereinkarnasi, yang ingin dia lakukan adalah menunjukkan kepada Jiang Yue betapa dia sangat mencintainya.\n" +
                        "\n" +
                        "Jika dia hanya menginginkan tiga hal, maka dia hanya menginginkan tiga hal: membangun kerajaan untuknya, untuk menghabiskan lebih banyak waktu bersamanya, dan menjalani kehidupan yang bahagia bersamanya.\n" +
                        "\n" +
                        "-----\n" +
                        "\n" +
                        "Dicampur dengan sedikit petunjuk misteri dan rahasia yang membingungkan, apa yang terjadi ketika dua orang yang bereinkarnasi bertemu dan jatuh cinta?\n" +
                        "\n" +
                        "-----\n" +
                        "\n" +
                        "\"Apakah Anda percaya pada reinkarnasi?\" Pertanyaan Fu Jin langsung membuat Jiang Yue membeku. Dia ingat bagaimana dia yang mengajukan pertanyaan ini ketika mereka pertama kali bertemu.\n" +
                        "\n" +
                        "\"Tentu saja aku tahu.\" Dia menjawab. Dia adalah bukti hidup bahwa reinkarnasi ada. \"Kenapa kamu menanyakan itu?\"\n" +
                        "\n" +
                        "\"Yah ... jika aku memberitahumu bahwa aku bereinkarnasi hanya untuk menjadikanmu milikku, apakah kamu percaya padaku?\" Pertanyaannya segera memberi merinding Jiang Yue.\n" +
                        "\n" +
                        "Dia tidak menjawab, sebaliknya, dia berbalik sehingga dia bisa menghadapinya.\n" +
                        "\n" +
                        "Pandangan mereka bertemu.\n" +
                        "\n" +
                        "\"Lalu jika kamu diberi kesempatan untuk bereinkarnasi,\n" +
                        "\n" +
                        "\"Tentu saja.\" Dia menjawab, ketika dia menyelipkan sehelai rambutnya yang longgar di belakang telinganya. \"Aku akan menemukanmu dan menunggumu Bu Fu. Bahkan jika itu akan memakan waktu jutaan tahun dan sejuta masa hidup.\"\n" +
                        "\n" +
                        "Jawaban Fu Jin langsung membuat Jiang Yue tersenyum. Namun, kali ini, cahaya misterius melintas di matanya.\n" +
                        "\n" +
                        "Mungkin, kata-kata Fu Jin benar.\n" +
                        "\n" +
                        "Mungkin, dia juga bereinkarnasi seperti dia.\n" +
                        "\n" +
                        "Dan mungkin ... mungkin saja, jiwa mereka sudah terhubung, jauh sebelum mereka bereinkarnasi.",
                "https://www.webnovel.com/book/12694808505649505"
        );

        tambahBuku(buku3, db);
        idBuku++;

        try {
            tempDate = sdFormat.parse("13/03/2020 06:22");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku4 = new Buku(
                idBuku,
                "Trial Marriage Husband: Need to Work Hard",
                tempDate,
                storeImagesFiles(R.drawable.buku4),
                "Contemporary Romance",
                "Passion Honey",
                "Malam sebelum pernikahan mereka, tunangannya lari bersama majikannya.\n" +
                        "Karena frustrasi, dia meraih pria yang berdiri di depan Kantor Urusan Sipil, \"Presiden Mo, pengantinmu belum tiba dan pengantin priaku sudah kabur ... Bolehkah aku menyarankan kita menikah?\"\n" +
                        "Sebelum menikah, dia berkata, \"Bahkan jika kita berbagi ranjang yang sama, tidak akan ada yang terjadi di antara kita!\"\n" +
                        "Setelah menikah, dia berkata, \"Jika kita tidak mencoba, bagaimana kita tahu?\"",
                "https://www.webnovel.com/book/8212987205006305/Trial-Marriage-Husband%3A-Need-to-Work-Hard"
        );

        tambahBuku(buku4, db);
        idBuku++;



    }
}
