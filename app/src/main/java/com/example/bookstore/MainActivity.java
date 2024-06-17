package com.example.bookstore;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "library";
    private static SQLiteDatabase productDatabase;

    private Button btn_search;
    private Button btnTaskPage;
    private Button btnPublishTaskPage;

    private ImageButton btnHome;
    private ImageButton btnSearch;
    private ImageButton btnCalendar;
    private ImageButton btnPersonal;

    private ListView lvBook;
    private EditText etSearch;


    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productDatabase = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        CreateTabke(productDatabase);
        ReplaceUserData(productDatabase);
        ReplaceBookData(productDatabase);
        //ReplaceTaskData(productDatabase);
        //productDatabase.execSQL("DELETE FROM task_list;");

        lvBook = findViewById(R.id.lv_mainpage_show_book);

        Image1 =findViewById(R.id.main_iv_1);
        Image2 =findViewById(R.id.main_iv_2);
        Image3 =findViewById(R.id.main_iv_3);

        Image1.setImageResource(R.drawable.walkleaf);
        Image2.setImageResource(R.drawable.green_seed);
        Image3.setImageResource(R.drawable.scallion_duck);

        Cursor cursor = productDatabase.rawQuery("SELECT book_id,name,author,publication_date,introduction,book_type,publication,picture FROM books",null);
        cursor.moveToFirst();

        SearchBook(cursor);

        showNavigationFragment();

    }
    public static class MainNavigationFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_navigation, container, false);

            ImageButton btnHome= view.findViewById(R.id.btn_home);
            ImageButton btnSearch = view.findViewById(R.id.btn_searchbook_navigate);
            ImageButton btnCalendar = view.findViewById(R.id.btn_calendar);
            ImageButton btnPersonal = view.findViewById(R.id.btn_personal);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(v.getId()==R.id.btn_home){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }else if(v.getId()==R.id.btn_searchbook_navigate){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Search_book.class);
                        startActivity(intent);
                    }
                    else if(v.getId()==R.id.btn_calendar){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Task_list.class);
                        startActivity(intent);
                    }
                    else if(v.getId()==R.id.btn_personal){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), show_book.class);
                        startActivity(intent);
                    }
                }
            };
            btnHome.setOnClickListener(listener);
            btnSearch.setOnClickListener(listener);
            btnCalendar.setOnClickListener(listener);
            btnPersonal.setOnClickListener(listener);
            return view;
        }
    }
    public void showNavigationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_main, new MainNavigationFragment());
        fragmentTransaction.commit();
    }
    public SQLiteDatabase getProductDatabase() {
        return productDatabase;
    }

    public void CreateTabke(SQLiteDatabase productDatabase){
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (\n" +
                "    user_id VARCHAR(20) PRIMARY KEY,\n" +
                "    password VARCHAR(255),\n" +
                "    name VARCHAR(10),\n" +
                "    phone CHAR(10),\n" +
                "    mail VARCHAR(30),\n" +
                "    department VARCHAR(20),\n" +
                "    grade INT\n" +
                ");");
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS books (\n" +
                "    book_id VARCHAR(12) PRIMARY KEY,\n" +
                "    name VARCHAR(30),\n" +
                "    author VARCHAR(30),\n" +
                "    publication VARCHAR(50),\n" +
                "    publication_date DATE,\n" +
                "    introduction TEXT,\n" +
                "    book_type VARCHAR(255),\n" +
                "    picture VARCHAR(30)\n" +
                ");");
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS Borrowing_list (\n" +
                "    borrowing_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    book_id VARCHAR(10),\n" +
                "    Borrower_id VARCHAR(20),\n" +
                "    Borrowing_date DATE,\n" +
                "    return_date DATE,\n" +
                "    FOREIGN KEY (book_id) REFERENCES books(book_id),\n" +
                "    FOREIGN KEY (Borrower_id) REFERENCES users(user_id)\n" +
                ");");

        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS task_list (\n" +
                "    task_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    release_date DATE,\n" +//任務日期
                "    task_start_time TIME,\n" +
                "    task_content TEXT,\n" +
                "    task_end_time TIME,\n" +
                "    publisher_id VARCHAR(20),\n" +
                "    number_of_recruits INT,\n" +
                "    FOREIGN KEY (publisher_id) REFERENCES users(user_id)\n" +
                ");");
        //productDatabase.execSQL("ALTER TABLE task_list ADD COLUMN task_start_time TIME DEFAULT '00:00:00';");
        //productDatabase.execSQL("ALTER TABLE task_list ADD COLUMN task_end_time TIME DEFAULT '00:00:00';");
        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS personal_tasks (\n" +
                "    receiver_id VARCHAR(20),\n" +
                "    task_id INT,\n" +
                "    PRIMARY KEY (receiver_id, task_id),\n" +
                "    FOREIGN KEY (receiver_id) REFERENCES users(user_id),\n" +
                "    FOREIGN KEY (task_id) REFERENCES task_list(task_id)\n" +
                ");");

        productDatabase.execSQL("CREATE TABLE IF NOT EXISTS user_borrowing (\n" +
                "    borrowing_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_id VARCHAR(20),\n" +
                "    book_id VARCHAR(12),\n" +
                "    borrowing_date DATE,\n" +
                "    return_date DATE,\n" +
                "    FOREIGN KEY (user_id) REFERENCES users(user_id),\n" +
                "    FOREIGN KEY (book_id) REFERENCES books(book_id)\n" +
                ");");
    }
    public void ReplaceTaskData(SQLiteDatabase productDatabase){
        productDatabase.execSQL("REPLACE INTO task_list (release_date, task_start_time,task_end_time,task_content, publisher_id, number_of_recruits) VALUES\n" +
                /*"('2024-06-02','07:00:00','08:00:00','課輔小老師', 'u001', 3),\n" +
                "('2024-06-02','13:00:00','14:00:00','音樂', 'u001', 3),\n" +
                "('2024-06-02','07:00:00','09:00:00','家政', 'u002', 3),\n" +
                "('2024-06-06','16:00:00','17:00:00','體育', 'u001', 3),\n" +
                "('2024-06-06','20:00:00','21:00:00','美勞', 'u002', 3),\n" +
                "('2024-06-07','12:00:00','14:00:00','家政', 'u001', 1),\n" +
                "('2024-06-07','07:00:00','08:00:00','課輔小老師', 'u001', 1),\n" +*/
                "('2024-06-07','09:00:00','10:00:00','手做', 'u001', 3),\n" +
                "('2024-06-08','19:00:00','20:00:00','數學小老師', 'u002', 2);");
    }
    public void ReplaceUserData(SQLiteDatabase productDatabase){
        productDatabase.execSQL("REPLACE INTO users (user_id, password, name, phone, mail, department, grade)VALUES('u001', 'p0000001', 'John Doe', '1234567890', 'john@example.com', 'IT', 3),('u002', 'p0000002', 'Jane Smith', '9876543210', 'jane@example.com', 'HR', 2);");
    }
    public void ReplaceBookData(SQLiteDatabase productDatabase){
        productDatabase.execSQL("REPLACE INTO books (book_id, name, author, publication, publication_date, introduction, book_type, picture) VALUES\n" +
                "('b000000001', '心靈的航程','Gracen Lee', 'Fantastic Reads', '2022-05-01', '一本令人著迷的奇幻小說', '奇幻', 'img01'),\n" +
                "('b000000002', '永恆的諾言', 'Hannah Smith', 'Mystery House', '2021-03-15', '扣人心弦的懸疑故事', '懸疑', 'img02'),\n" +
                "('b000000003', '星空下的回憶', 'John Doe', 'Science World', '2020-11-23', '探索科學奧秘的精采作品', '科學', 'img03'),\n" +
                "('b000000004', '靜謐的花園', 'Jane Austen', 'Classic Literature', '1813-01-28', '永垂不朽的經典文學', '經典', 'img04'),\n" +
                "('b000000005', '夢想的彼岸', 'Mark Twain', 'Adventure Books', '1884-12-10', '充滿冒險精神的故事', '冒險', 'img05'),\n" +
                "('b000000006', '孤獨的探險家', 'Emily Brontë', 'Gothic Tales', '1847-12-17', '哥德式小說的代表作', '哥德', 'img06'),\n" +
                "('b000000007', '秘密的日記', 'Leo Tolstoy', 'Historical Fiction', '1869-01-01', '描寫歷史事件的小說', '歷史小說', 'img07'),\n" +
                "('b000000008', '時光的沙漏', 'George Orwell', 'Dystopian Novels', '1949-06-08', '對未來的反烏托邦預言', '反烏托邦', 'img08'),\n" +
                "('b000000009', '遠方的呼喚', 'J.K. Rowling', 'Fantasy World', '1997-06-26', '深受喜愛的魔法世界', '奇幻', 'img09'),\n" +
                "('b000000010', '愛的旋律', 'Isaac Asimov', 'Science Fiction', '1950-01-01', '科幻文學的經典之作', '科幻', 'img10'),\n" +
                "('b000000011', '迷霧中的真相', 'Terry Pratchett', 'Discworld Publishing', '1983-11-24', '幽默且引人入勝的奇幻故事', '奇幻', 'img11'),\n" +
                "('b000000012', '無盡的探索', 'Stephen King', 'Horror House', '1977-01-28', '一個令人毛骨悚然的恐怖故事', '恐怖', 'img12'),\n" +
                "('b000000013', '古老的傳說', 'Margaret Atwood', 'Dystopian Press', '1985-09-01', '描寫女性在壓迫社會中的生存故事', '反烏托邦', 'img13'),\n" +
                "('b000000014', '生命的樂章', 'Douglas Adams', 'Sci-Fi Classics', '1979-10-12', '一部幽默的科幻冒險小說', '科幻', 'img14'),\n" +
                "('b000000015', '遙遠的星辰', 'C.S. Lewis', 'Fantasy Adventures', '1950-10-16', '一段奇幻的冒險故事', '奇幻', 'img15'),\n" +
                "('b000000016', '心靈的窗口', 'Kurt Vonnegut', 'Satirical Novels', '1969-03-31', '一部黑色幽默的反戰小說', '諷刺', 'img16'),\n" +
                "('b000000017', '月光下的舞者', 'J.D. Salinger', 'Modern Classics', '1951-07-16', '一個年輕人成長的故事', '經典', 'img17'),\n" +
                "('b000000018', '流浪者的歸途', 'George R.R. Martin', 'Epic Fantasy', '1996-08-06', '史詩般的奇幻世界', '奇幻', 'img18'),\n" +
                "('b000000019', '命運的交織', 'Neil Gaiman', 'Urban Fantasy', '2001-06-19', '融合神話與現代的奇幻故事', '奇幻', 'img19'),\n" +
                "('b000000020', '時光的痕跡', 'Aldous Huxley', 'Dystopian Press', '1932-01-01', '預言未來社會的反烏托邦小說', '反烏托邦', 'img20'),\n" +
                "('b000000021', '靈魂的旅程', 'Orson Scott Card', 'Sci-Fi Classics', '1985-01-15', '一個少年成為戰爭英雄的科幻故事', '科幻', 'img21'),\n" +
                "('b000000022', '森林中的秘密', 'Philip K. Dick', 'Future Worlds', '1968-01-01', '探索人性與未來科技的小說', '科幻', 'img22'),\n" +
                "('b000000023', '夢中的王國', 'Suzanne Collins', 'Dystopian Press', '2008-09-14', '一個反抗暴政的冒險故事', '反烏托邦', 'img23'),\n" +
                "('b000000024', '未來的啟示', 'Jules Verne', 'Adventure Classics', '1873-01-01', '環遊世界的驚險旅程', '冒險', 'img24'),\n" +
                "('b000000025', '古城的記憶', 'Herman Melville', 'Classic Literature', '1851-10-18', '一個海上冒險和復仇的故事', '經典', 'img25'),\n" +
                "('b000000026', '愛的呼喚', 'Ernest Hemingway', 'Modern Classics', '1952-09-01', '一個老漁夫與大魚搏鬥的故事', '經典', 'img26'),\n" +
                "('b000000027', '黎明前的黑暗', 'Thomas Hardy', 'Classic Romance', '1874-01-01', '關於愛與命運的經典故事', '經典', 'img27'),\n" +
                "('b000000028', '海洋的故事', 'Virginia Woolf', 'Modernist Literature', '1925-05-14', '探索內心世界的現代主義小說', '經典', 'img28'),\n" +
                "('b000000029', '心靈的救贖', 'Jack London', 'Adventure Books', '1903-01-01', '一個狗在荒野中的生存故事', '冒險', 'img29'),\n" +
                "('b000000030', '星際探險', 'Toni Morrison', 'Contemporary Fiction', '1987-01-01', '關於種族與家庭的深刻故事', '當代文學', 'img30'),\n" +
                "('b000000031', '雪夜的奇蹟', 'Khaled Hosseini', 'Riverhead Books', '2003-05-29', '關於友誼與背叛的感人故事', '小說', 'img31'),\n" +
                "('b000000032', '秘密的花園', 'Chimamanda Ngozi Adichie', 'Fourth Estate', '2013-05-14', '探索種族、性別與身份的小說', '小說', 'img32'),\n" +
                "('b000000033', '光明與黑暗', 'Paulo Coelho', 'HarperOne', '1988-05-01', '追尋夢想的寓言故事', '哲理小說', 'img33'),\n" +
                "('b000000034', '靈魂的共鳴', 'Haruki Murakami', 'Shinchosha', '1987-09-04', '一個關於愛與孤獨的故事', '小說', 'img34'),\n" +
                "('b000000035', '穿越時空的愛', 'Zadie Smith', 'Penguin Books', '2000-01-27', '探索家庭與文化身份的小說', '小說', 'img35'),\n" +
                "('b000000036', '永恆的傳奇', 'Elena Ferrante', 'Europa Editions', '2011-10-19', '一段複雜的女性友誼故事', '小說', 'img36'),\n" +
                "('b000000037', '心靈的詩篇', 'Yann Martel', 'Harcourt', '2001-09-11', '一個少年與老虎的海上冒險', '小說', 'img37'),\n" +
                "('b000000038', '夢想的城堡', 'Donna Tartt', 'Little, Brown and Company', '2013-10-22', '一個關於藝術與犯罪的引人入勝的故事', '小說', 'img38'),\n" +
                "('b000000039', '未來的光芒', 'Kazuo Ishiguro', 'Faber and Faber', '1989-05-01', '探索記憶與遺憾的故事', '小說', 'img39'),\n" +
                "('b000000040', '愛的旅程', 'Jhumpa Lahiri', 'Houghton Mifflin Harcourt', '1999-01-01', '探索移民家庭生活的短篇故事集', '小說', 'img40'),\n" +
                "('b000000041', '遺失的記憶', 'Gabriel García Márquez', 'Harper & Row', '1970-03-05', '描寫家族命運與魔幻現實的故事', '魔幻現實主義', 'img41'),\n" +
                "('b000000042', '時空的旅者', 'Isabel Allende', 'Knopf', '1982-05-05', '一個跨越時代與文化的家族故事', '小說', 'img42'),\n" +
                "('b000000043', '星際迷航', 'Milan Kundera', 'Harper & Row', '1984-01-01', '探討存在與身份的哲理小說', '哲理小說', 'img43'),\n" +
                "('b000000044', '生命的奇蹟', 'Ian McEwan', 'Jonathan Cape', '2001-09-01', '一個愛與戰爭的故事', '小說', 'img44'),\n" +
                "('b000000045', '心靈的歸宿', 'Michael Ondaatje', 'McClelland & Stewart', '1992-09-01', '一個二戰後的愛情故事', '小說', 'img45'),\n" +
                "('b000000046', '夢中的彩虹', 'Alice Munro', 'McClelland & Stewart', '2004-04-15', '探索人性與日常生活的短篇故事集', '小說', 'img46'),\n" +
                "('b000000047', '秘密的遺產', 'Vladimir Nabokov', 'Putnam', '1955-08-18', '一個關於愛與迷戀的爭議性故事', '小說', 'img47'),\n" +
                "('b000000048', '光影的舞動', 'E.M. Forster', 'Edward Arnold', '1924-01-01', '一個文化衝突與友誼的故事', '小說', 'img48'),\n" +
                "('b000000049', '穿越時空的旅人', 'William Faulkner', 'Random House', '1936-01-01', '探索南方社會的經典文學', '小說', 'img49'),\n" +
                "('b000000050', '愛的綻放', 'Edith Wharton', 'Charles Scribner', '1920-01-01', '一個探討社會階級與愛情的故事', '小說', 'img50'),\n" +
                "('b000000051', '靈魂的重生', 'J.D. Salinger', 'Little, Brown and Company', '1951-07-16', '探索青春與孤獨的經典小說', '小說', 'img51'),\n" +
                "('b000000052', '遠古的謎團', 'Emily Brontë', 'Thomas Cautley Newby', '1847-12-17', '哥德風格的經典愛情故事', '經典', 'img52'),\n" +
                "('b000000053', '星空的約定', 'F. Scott Fitzgerald', 'Charles Scribner', '1925-04-10', '美國夢的破滅與愛情故事', '經典', 'img53'),\n" +
                "('b000000054', '命運的選擇', 'John Steinbeck', 'The Viking Press', '1939-04-14', '描述大蕭條時期的家庭奮鬥', '小說', 'img54'),\n" +
                "('b000000055', '心靈的呼喚', 'Toni Morrison', 'Alfred A. Knopf', '1987-09-15', '探索種族和身份的深刻故事', '小說', 'img55'),\n" +
                "('b000000056', '夢想的曙光', 'Markus Zusak', 'Picador', '2005-03-14', '二戰期間一個少女的動人故事', '歷史小說', 'img56'),\n" +
                "('b000000057', '未來的傳承', 'John Green', 'Dutton Books', '2012-01-10', '關於愛與失落的青少年小說', '小說', 'img57'),\n" +
                "('b000000058', '秘密的信件', 'Kazuo Ishiguro', 'Faber and Faber', '2005-03-03', '一個探討人性與命運的故事', '科幻', 'img58'),\n" +
                "('b000000059', '光芒的追尋', 'Donna Tartt', 'Little, Brown and Company', '1992-09-04', '關於青少年犯罪的懸疑小說', '懸疑', 'img59'),\n" +
                "('b000000060', '穿越時空的秘密', 'Hanya Yanagihara', 'Doubleday', '2015-03-10', '一段深刻的友誼與創傷的故事', '小說', 'img60'),\n" +
                "('b000000061', '愛的奇蹟', 'Ernest Hemingway', 'Charles Scribner', '1926-10-21', '一個對戰爭的沉痛反思', '經典', 'img61'),\n" +
                "('b000000062', '靈魂的契約', 'Leo Tolstoy', 'The Russian Messenger', '1869-01-01', '描寫家族與愛情的俄羅斯史詩', '經典', 'img62'),\n" +
                "('b000000063', '星辰的記憶', 'Victor Hugo', 'A. Lacroix, Verboeckhoven & Cie.', '1862-03-15', '法國大革命的浪漫主義小說', '經典', 'img63'),\n" +
                "('b000000064', '時光的旋律', 'Charlotte Brontë', 'Smith, Elder & Co.', '1847-10-16', '一個女孩在鄉村孤兒院的成長故事', '經典', 'img64'),\n" +
                "('b000000065', '心靈的曙光', 'Mary Shelley', 'Lackington, Hughes, Harding, Mavor & Jones', '1818-01-01', '一個創造怪物的科學恐怖故事', '經典', 'img65'),\n" +
                "('b000000066', '夢中的奇幻', 'George Orwell', 'Secker & Warburg', '1949-06-08', '對極權主義社會的警示', '經典', 'img66'),\n" +
                "('b000000067', '秘密的寶藏', 'Ray Bradbury', 'Ballantine Books', '1953-10-19', '禁止閱讀的未來社會的警示故事', '經典', 'img67'),\n" +
                "('b000000068', '光與影的交響', 'Gabriel García Márquez', 'Editorial Sudamericana', '1967-05-30', '一個魔幻寫實主義的家族傳奇', '經典', 'img68'),\n" +
                "('b000000069', '穿越時空的探索', 'J.K. Rowling', 'Bloomsbury Publishing', '1997-06-26', '一個魔法世界的冒險故事', '經典', 'img69'),\n" +
                "('b000000070', '愛的希望', 'Isaac Asimov', 'Gnome Press', '1950-05-21', '一個由機器人引發的未來人類的故事', '經典', 'img70'),\n" +
                "('b000000071', '靈魂的啟示', 'Jane Austen', 'Thomas Egerton', '1813-01-28', '一個關於愛情與社會風俗的愛情小說', '經典', 'img71'),\n" +
                "('b000000072', '遠古的呼喚', 'Harper Lee', 'J.B. Lippincott & Co.', '1960-07-11', '一個關於種族與家庭的故事', '經典', 'img72'),\n" +
                "('b000000073', '星空的探險', 'Margaret Mitchell', 'Macmillan Publishers', '1936-06-30', '一個關於南北戰爭時期的愛情故事', '經典', 'img73'),\n" +
                "('b000000074', '命運的交響曲', 'Toni Morrison', 'Alfred A. Knopf', '1977-01-01', '一個關於奴隸制度與自由的故事', '經典', 'img74'),\n" +
                "('b000000075', '心靈的謎題', 'Ray Bradbury', 'Doubleday & Company', '1950-10-19', '一個關於焚書與自由的科幻小說', '經典', 'img75'),\n" +
                "('b000000076', '夢想的花園', 'F. Scott Fitzgerald', 'Charles Scribner', '1922-04-10', '一個關於美國夢的破滅與成就的故事', '經典', 'img76'),\n" +
                "('b000000077', '未來的預言', 'George Orwell', 'Secker & Warburg', '1945-06-08', '一個關於極權主義的寓言故事', '經典', 'img77'),\n" +
                "('b000000078', '秘密的故事', 'Mark Twain', 'Charles L. Webster And Company', '1884-12-10', '一個關於南北戰爭時期的冒險故事', '經典', 'img78'),\n" +
                "('b000000079', '光的追尋', 'Ernest Hemingway', 'Charles Scribner', '1926-10-21', '一個關於第一次世界大戰的戰爭小說', '經典', 'img79'),\n" +
                "('b000000080', '穿越時空的使命', 'Leo Tolstoy', 'The Russian Messenger', '1869-01-01', '一個關於俄羅斯貴族家庭的史詩小說', '經典', 'img80'),\n" +
                "('b000000081', '愛的祈願', 'Virginia Woolf', 'Hogarth Press', '1925-10-20', '探索人性與自我認知的現代主義小說', '經典', 'img81'),\n" +
                "('b000000082', '靈魂的旅途', 'Gabriel García Márquez', 'Editorial Sudamericana', '1967-05-30', '一個關於愛情與家族的魔幻現實主義小說', '經典', 'img82'),\n" +
                "('b000000083', '遠古的回聲', 'Franz Kafka', 'Kurt Wolff Verlag', '1925-10-20', '一個關於個人身份與社會壓力的哲學小說', '經典', 'img83'),\n" +
                "('b000000084', '星辰的傳說', 'Fyodor Dostoevsky', 'The Russian Messenger', '1866-01-01', '一個關於罪惡與救贖的俄羅斯文學經典', '經典', 'img84'),\n" +
                "('b000000085', '時光的旅程', 'Hermann Hesse', 'Suhrkamp Verlag', '1922-09-01', '一個關於個人尋求靈性與自由的德國文學經典', '經典', 'img85'),\n" +
                "('b000000086', '心靈的花朵', 'Dante Alighieri', 'Folengo', '1308-01-01', '一個關於人類罪惡與救贖的意大利文學經典', '經典', 'img86'),\n" +
                "('b000000087', '夢中的奇遇', 'Homer', 'Hellenistic Period', '1954-07-29', '一個關於古希臘英雄歷險的史詩', '經典', 'img87'),\n" +
                "('b000000088', '秘密的計畫', 'J.R.R. Tolkien', 'Allen & Unwin', '1954-07-29', '一個關於中土世界的奇幻冒險故事', '經典', 'img88'),\n" +
                "('b000000089', '光明的召喚', 'Miguel de Cervantes', 'Juan de la Cuesta', '1605-01-16', '一個關於騎士精神與現實的西班牙文學經典', '經典', 'img89'),\n" +
                "('b000000090', '穿越時空的冒險', 'Leo Tolstoy', 'The Russian Messenger', '1877-01-01', '一個關於戰爭與和平的俄羅斯文學經典', '經典', 'img90'),\n" +
                "('b000000091', '愛的誓言', 'J.R.R. Tolkien', 'George Allen & Unwin', '1937-09-21', '一個關於霍比特人冒險的奇幻小說', '奇幻', 'img91'),\n" +
                "('b000000092', '靈魂的共振', 'C.S. Lewis', 'Geoffrey Bles', '1950-10-16', '一個關於奇幻世界的冒險故事', '奇幻', 'img92'),\n" +
                "('b000000093', '遠古的傳承', 'J.K. Rowling', 'Bloomsbury Publishing', '1997-06-26', '一個關於魔法世界的冒險故事', '奇幻', 'img93'),\n" +
                "('b000000094', '星空的探索', 'George Orwell', 'Secker and Warburg', '1945-06-08', '一個關於極權主義的寓言故事', '經典', 'img94'),\n" +
                "('b000000096', '命運的連結', 'Markus Zusak', 'Picador', '2005-03-14', '二戰期間一個少女的動人故事', '歷史小說', 'img96'),\n" +
                "('b000000097', '心靈的治癒', 'John Green', 'Dutton Books', '2012-01-10', '關於愛與失落的青少年小說', '小說', 'img97'),\n" +
                "('b000000098', '夢想的實現', 'Kazuo Ishiguro', 'Faber and Faber', '2005-03-03', '一個探討人性與命運的故事', '科幻', 'img98'),\n" +
                "('b000000099', '未來的光輝', 'Donna Tartt', 'Little, Brown and Company', '1992-09-04', '關於青少年犯罪的懸疑小說', '懸疑', 'img99'),\n" +
                "('b000000100', '光陰的故事', 'Hanya Yanagihara', 'Doubleday', '2015-03-10', '一段深刻的友誼與創傷的故事', '小說', 'img100');");
    }
    private void SearchBook(Cursor cursor){
        List<Book> books = new ArrayList<>();
        for(int i = 0; i < cursor.getCount(); i++){
            Book book = new Book(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),R.drawable.img01);
            books.add(book);
            cursor.moveToNext();
        }
        BooksAdapter adapter = new BooksAdapter(this,books);
        lvBook.setAdapter(adapter);
    }
}