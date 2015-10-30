import System.Environment   -- for command-line arguments


main = do
   args <- getArgs                      -- get list of command-line arguments
   mapM_ putStrLn $ convert $ last args -- last argument is string to convert to ascii art


{- convert a string into ascii art -}
convert :: String -> [String]
convert [] = ["", "", "", "", "", "", "", ""]   -- eight lines
convert (x:xs) = zipWith (++) (smallToLarge x) (convert xs)


{- turn a character into a five line ascii art representation -}
smallToLarge :: Char -> [String]
smallToLarge x = case x of ' ' -> ["    ", 
                                   "    ", 
                                   "    ", 
                                   "    ", 
                                   "    ", 
                                   "    ", 
                                   "    ", 
                                   "    "]
                           '!' -> [" _ ", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "|_|", 
                                   "(_)",
                                   "   ",
                                   "   "]
                           '"' -> [" _ _ ", 
                                   "( | )", 
                                   " V V ", 
                                   "     ", 
                                   "     ", 
                                   "     ",
                                   "     ",
                                   "     "]
                           '#' -> ["   _  _   ", 
                                   " _| || |_ ", 
                                   "|_  __  _|", 
                                   " _| || |_ ", 
                                   "|_  __  _|", 
                                   "  |_||_|  ",
                                   "          ",
                                   "          "]
                           '$' -> ["  _  ", 
                                   " | | ", 
                                   "/ __)", 
                                   "\\__ \\", 
                                   "(   /", 
                                   " |_| ",
                                   "     ",
                                   "     "]
                           '%' -> [" _   __", 
                                   "(_) / /", 
                                   "   / / ", 
                                   "  / /  ", 
                                   " / / _ ", 
                                   "/_/ (_)",
                                   "       ",
                                   "       "]
                           '&' -> ["  ___   ", 
                                   " ( _ )  ", 
                                   " / _ \\/\\", 
                                   "| (_>  <", 
                                   " \\___/\\/", 
                                   "        ",
                                   "        ",
                                   "        "]
                           '\'' -> [" _ ", 
                                    "( )", 
                                    "|/ ", 
                                    "   ", 
                                    "   ", 
                                    "   ",
                                    "   ",
                                    "   "]
                           '(' -> ["  __", 
                                   " / /", 
                                   "| | ", 
                                   "| | ",
                                   "| | ",
                                   "| | ", 
                                   " \\_\\",
                                   "    "]
                           ')' -> ["__  ", 
                                   "\\ \\ ", 
                                   " | |", 
                                   " | |",
                                   " | |",
                                   " | |", 
                                   "/_/ ",
                                   "    "]
                           '*' -> ["    _    ", 
                                   " /\\| |/\\ ", 
                                   " \\ ` \' / ", 
                                   "|_     _|", 
                                   " / , . \\ ", 
                                   " \\/|_|\\/ ",
                                   "         ",
                                   "         "]
                           '+' -> ["         ", 
                                   "    _    ", 
                                   "  _| |_  ", 
                                   " |_   _| ", 
                                   "   |_|   ", 
                                   "         ",
                                   "         ",
                                   "         "]
                           ',' -> ["   ", 
                                   "   ", 
                                   "   ", 
                                   " _ ", 
                                   "( )", 
                                   "|/ ",
                                   "   ",
                                   "   "]
                           '-' -> ["        ", 
                                   "        ", 
                                   " ______ ", 
                                   "|______|", 
                                   "        ", 
                                   "        ",
                                   "        ",
                                   "        "]
                           '.' -> ["   ", 
                                   "   ", 
                                   "   ", 
                                   "   ", 
                                   " _ ", 
                                   "(_)",
                                   "   ",
                                   "   "]
                           '/' -> ["     __", 
                                   "    / /", 
                                   "   / / ", 
                                   "  / /  ", 
                                   " / /   ", 
                                   "/_/    ",
                                   "       ",
                                   "       "]
                           '0' -> ["  ___  ", 
                                   " / _ \\ ", 
                                   "| | | |", 
                                   "| | | |", 
                                   "| |_| |", 
                                   " \\___/ ",
                                   "       ",
                                   "       "]
                           '1' -> [" __ ", 
                                   "/_ |", 
                                   " | |", 
                                   " | |", 
                                   " | |", 
                                   " |_|",
                                   "    ",
                                   "    "]
                           '2' -> [" ___  ", 
                                   "|__ \\ ", 
                                   "   ) |", 
                                   "  / / ", 
                                   " / /_ ", 
                                   "|____|",
                                   "      ",
                                   "      "]
                           '3' -> [" ____  ", 
                                   "|___ \\ ", 
                                   "  __) |", 
                                   " |__ < ", 
                                   " ___) |", 
                                   "|____/ ",
                                   "       ",
                                   "       "]
                           '4' -> [" _  _   ", 
                                   "| || |  ", 
                                   "| || |_ ", 
                                   "|__   _|", 
                                   "   | |  ", 
                                   "   |_|  ",
                                   "        ",
                                   "        "]
                           '5' -> [" _____ ", 
                                   "| ____|", 
                                   "| |__  ", 
                                   "|___ \\ ", 
                                   " ___) |", 
                                   "|____/ ",
                                   "       ",
                                   "       "]
                           '6' -> ["   __  ", 
                                   "  / /  ", 
                                   " / /_  ", 
                                   "| \'_ \\ ", 
                                   "| (_) |", 
                                   " \\___/ ",
                                   "       ",
                                   "       "]
                           '7' -> [" ______ ", 
                                   "|____  |", 
                                   "    / / ", 
                                   "   / /  ", 
                                   "  / /   ", 
                                   " /_/    ",
                                   "        ",
                                   "        "]
                           '8' -> ["  ___  ", 
                                   " / _ \\ ", 
                                   "| (_) |", 
                                   " > _ < ", 
                                   "| (_) |", 
                                   " \\___/ ",
                                   "       ",
                                   "       "]
                           '9' -> ["  ___  ", 
                                   " / _ \\ ", 
                                   "| (_) |", 
                                   " \\__, |", 
                                   "   / / ", 
                                   "  /_/  ",
                                   "       ",
                                   "       "]
                           ':' -> [" _ ", 
                                   "(_)", 
                                   "   ", 
                                   " _ ", 
                                   "(_)", 
                                   "   ",
                                   "   ",
                                   "   "]
                           ';' -> [" _ ", 
                                   "(_)", 
                                   "   ", 
                                   " _ ", 
                                   "( )", 
                                   "|/ ",
                                   "   ",
                                   "   "]
                           '<' -> ["   __", 
                                   "  / /", 
                                   " / / ", 
                                   "< <  ", 
                                   " \\ \\ ", 
                                   "  \\_\\",
                                   "     ",
                                   "     "]
                           '=' -> ["        ", 
                                   " ______ ", 
                                   "|______|", 
                                   " ______ ", 
                                   "|______|", 
                                   "        ",
                                   "        ",
                                   "        "]
                           '>' -> ["__   ", 
                                   "\\ \\  ", 
                                   " \\ \\ ", 
                                   "  > >", 
                                   " / / ", 
                                   "/_/  ",
                                   "     ",
                                   "     "]
                           '?' -> [" ___  ", 
                                   "|__ \\ ", 
                                   "   ) |", 
                                   "  / / ", 
                                   " |_|  ", 
                                   " (_)  ",
                                   "      ",
                                   "      "]
                           '@' -> ["   ____  ", 
                                   "  / __ \\ ", 
                                   " / / _` |", 
                                   "| | (_| |", 
                                   " \\ \\__,_|",
                                   "  \\____/ ",
                                   "         ",
                                   "         "]
                           'A' -> ["          ", 
                                   "    /\\    ", 
                                   "   /  \\   ", 
                                   "  / /\\ \\  ", 
                                   " / ____ \\ ", 
                                   "/_/    \\_\\",
                                   "          ",
                                   "          "]
                           'B' -> [" ____  ", 
                                   "|  _ \\ ", 
                                   "| |_) |", 
                                   "|  _ < ", 
                                   "| |_) |", 
                                   "|____/ ",
                                   "       ",
                                   "       "]
                           'C' -> ["  _____ ", 
                                   " / ____|", 
                                   "| |     ", 
                                   "| |     ", 
                                   "| |____ ", 
                                   " \\_____|",
                                   "        ",
                                   "        "]
                           'D' -> [" _____  ", 
                                   "|  __ \\ ", 
                                   "| |  | |", 
                                   "| |  | |", 
                                   "| |__| |", 
                                   "|_____/ ",
                                   "        ",
                                   "        "]
                           'E' -> [" ______ ", 
                                   "|  ____|", 
                                   "| |__   ", 
                                   "|  __|  ", 
                                   "| |____ ", 
                                   "|______|",
                                   "        ",
                                   "        "]
                           'F' -> [" ______ ", 
                                   "|  ____|", 
                                   "| |__   ", 
                                   "|  __|  ", 
                                   "| |     ", 
                                   "|_|     ",
                                   "        ",
                                   "        "]
                           'G' -> ["  _____ ", 
                                   " / ____|", 
                                   "| |  __ ", 
                                   "| | |_ |", 
                                   "| |__| |", 
                                   " \\_____|",
                                   "        ",
                                   "        "]
                           'H' -> [" _    _ ", 
                                   "| |  | |", 
                                   "| |__| |", 
                                   "|  __  |", 
                                   "| |  | |", 
                                   "|_|  |_|",
                                   "        ",
                                   "        "]
                           'I' -> [" _____ ", 
                                   "|_   _|", 
                                   "  | |  ", 
                                   "  | |  ", 
                                   " _| |_ ", 
                                   "|_____|",
                                   "        ",
                                   "        "]
                           'J' -> ["      _ ", 
                                   "     | |", 
                                   "     | |", 
                                   " _   | |", 
                                   "| |__| |", 
                                   " \\____/",
                                   "        ",
                                   "        "]
                           'K' -> [" _  __", 
                                   "| |/ /", 
                                   "| \' / ", 
                                   "|  <  ", 
                                   "| . \\ ", 
                                   "|_|\\_\\",
                                   "      ",
                                   "      "]
                           'L' -> [" _      ", 
                                   "| |     ", 
                                   "| |     ", 
                                   "| |     ", 
                                   "| |____ ", 
                                   "|______|",
                                   "        ",
                                   "        "]
                           'M' -> [" __  __ ", 
                                   "|  \\/  |", 
                                   "| \\  / |", 
                                   "| |\\/| |", 
                                   "| |  | |", 
                                   "|_|  |_|",
                                   "        ",
                                   "        "]
                           'N' -> [" _   _ ", 
                                   "| \\ | |", 
                                   "|  \\| |", 
                                   "| . ` |", 
                                   "| |\\  |", 
                                   "|_| \\_|",
                                   "       ",
                                   "       "]
                           'O' -> ["  ____  ", 
                                   " / __ \\ ", 
                                   "| |  | |", 
                                   "| |  | |", 
                                   "| |__| |", 
                                   " \\____/ ",
                                   "        ",
                                   "        "]
                           'P' -> [" _____  ", 
                                   "|  __ \\ ", 
                                   "| |__) |", 
                                   "|  ___/ ", 
                                   "| |     ", 
                                   "|_|     ",
                                   "        ",
                                   "        "]
                           'Q' -> ["  ____  ", 
                                   " / __ \\ ", 
                                   "| |  | |", 
                                   "| |  | |", 
                                   "| |__| |", 
                                   " \\___\\_\\",
                                   "        ",
                                   "        "]
                           'R' -> [" _____  ", 
                                   "|  __ \\ ", 
                                   "| |__) |", 
                                   "|  _  / ", 
                                   "| | \\ \\ ", 
                                   "|_|  \\_\\",
                                   "        ",
                                   "        "]
                           'S' -> ["  _____ ", 
                                   " / ____|", 
                                   "| (___  ", 
                                   " \\___ \\ ", 
                                   " ____) |", 
                                   "|_____/ ",
                                   "        ",
                                   "        "]
                           'T' -> [" _______ ", 
                                   "|__   __|", 
                                   "   | |   ", 
                                   "   | |   ", 
                                   "   | |   ", 
                                   "   |_|   ",
                                   "         ",
                                   "         "]
                           'U' -> [" _    _ ", 
                                   "| |  | |", 
                                   "| |  | |", 
                                   "| |  | |", 
                                   "| |__| |", 
                                   " \\____/ ",
                                   "        ",
                                   "        "]
                           'V' -> ["__      __", 
                                   "\\ \\    / /", 
                                   " \\ \\  / / ", 
                                   "  \\ \\/ /  ", 
                                   "   \\  /   ", 
                                   "    \\/    ",
                                   "          ",
                                   "          "]
                           'W' -> ["__          __", 
                                   "\\ \\        / /", 
                                   " \\ \\  /\\  / / ", 
                                   "  \\ \\/  \\/ /  ", 
                                   "   \\  /\\  /   ", 
                                   "    \\/  \\/    ",
                                   "              ",
                                   "              "]
                           'X' -> ["__   __", 
                                   "\\ \\ / /", 
                                   " \\ V / ", 
                                   "  > <  ", 
                                   " / . \\ ", 
                                   "/_/ \\_\\",
                                   "       ",
                                   "       "]
                           'Y' -> ["__     __", 
                                   "\\ \\   / /", 
                                   " \\ \\_/ / ", 
                                   "  \\   /  ", 
                                   "   | |   ", 
                                   "   |_|   ",
                                   "         ",
                                   "         "]
                           'Z' -> [" ______", 
                                   "|___  /", 
                                   "   / / ", 
                                   "  / /  ", 
                                   " / /__ ", 
                                   "/_____|",
                                   "       ",
                                   "       "]
                           '[' -> [" ___ ", 
                                   "|  _|", 
                                   "| |  ", 
                                   "| |  ",
                                   "| |  ",
                                   "| |_ ", 
                                   "|___|",
                                   "     "]
                           '\\' -> ["__     ", 
                                    "\\ \\    ", 
                                    " \\ \\   ", 
                                    "  \\ \\  ", 
                                    "   \\ \\ ", 
                                    "    \\_\\",
                                   "        ",
                                   "        "]
                           ']' -> [" ___ ", 
                                   "|_  |", 
                                   "  | |", 
                                   "  | |",
                                   "  | |",
                                   " _| |", 
                                   "|___|",
                                   "     "]
                           '^' -> [" /\\ ", 
                                   "|/\\|", 
                                   "    ", 
                                   "    ", 
                                   "    ", 
                                   "    ",
                                   "    ",
                                   "    "]
                           '_' -> ["        ", 
                                   "        ", 
                                   "        ", 
                                   "        ", 
                                   " ______ ", 
                                   "|______|",
                                   "        ",
                                   "        "]
                           '`' -> [" _ ", 
                                   "( )", 
                                   " \\|", 
                                   "   ", 
                                   "   ", 
                                   "   ",
                                   "   ",
                                   "   "]
                           'a' -> ["       ", 
                                   "       ", 
                                   "  __ _ ", 
                                   " / _` |", 
                                   "| (_| |", 
                                   " \\__,_|",
                                   "       ",
                                   "       "]
                           'b' -> [" _     ", 
                                   "| |    ", 
                                   "| |__  ", 
                                   "| \'_ \\ ", 
                                   "| |_) |", 
                                   "|_.__/ ",
                                   "       ",
                                   "       "]
                           'c' -> ["      ", 
                                   "      ", 
                                   "  ___ ", 
                                   " / __|", 
                                   "| (__ ", 
                                   " \\___|",
                                   "      ",
                                   "      "]
                           'd' -> ["     _ ", 
                                   "    | |", 
                                   "  __| |", 
                                   " / _` |", 
                                   "| (_| |", 
                                   " \\__,_|",
                                   "       ",
                                   "       "]
                           'e' -> ["      ", 
                                   "      ", 
                                   "  ___ ", 
                                   " / _ \\", 
                                   "|  __/", 
                                   " \\___|",
                                   "      ",
                                   "      "]
                           'f' -> ["  __ ", 
                                   " / _|", 
                                   "| |_ ", 
                                   "|  _|", 
                                   "| |  ", 
                                   "|_|  ",
                                   "     ",
                                   "     "]
                           'g' -> ["       ", 
                                   "       ", 
                                   "  __ _ ", 
                                   " / _` |", 
                                   "| (_| |", 
                                   " \\__, |",
                                   "  __/ |",
                                   " |___/ "]
                           'h' -> [" _     ", 
                                   "| |    ", 
                                   "| |__  ", 
                                   "| \'_ \\ ", 
                                   "| | | |", 
                                   "|_| |_|",
                                   "       ",
                                   "       "]
                           'i' -> [" _ ", 
                                   "(_)", 
                                   " _ ", 
                                   "| |", 
                                   "| |", 
                                   "|_|", 
                                   "   ", 
                                   "   "]
                           'j' -> ["   _ ", 
                                   "  (_)", 
                                   "   _ ", 
                                   "  | |", 
                                   "  | |", 
                                   "  | |", 
                                   " _/ |", 
                                   "|__/ "]
                           'k' -> [" _    ", 
                                   "| |   ", 
                                   "| | __", 
                                   "| |/ /", 
                                   "|   < ", 
                                   "|_|\\_\\", 
                                   "      ", 
                                   "      "]
                           'l' -> [" _ ", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "|_|", 
                                   "   ", 
                                   "   "]
                           'm' -> ["           ", 
                                   "           ", 
                                   " _ __ ___  ", 
                                   "| \'_ ` _ \\ ", 
                                   "| | | | | |", 
                                   "|_| |_| |_|", 
                                   "           ", 
                                   "           "]
                           'n' -> ["       ", 
                                   "       ", 
                                   " _ __  ", 
                                   "| \'_ \\ ", 
                                   "| | | |", 
                                   "|_| |_|", 
                                   "       ", 
                                   "       "]
                           'o' -> ["       ", 
                                   "       ", 
                                   "  ___  ", 
                                   " / _ \\ ", 
                                   "| (_) |", 
                                   " \\___/ ", 
                                   "       ", 
                                   "       "]
                           'p' -> ["       ", 
                                   "       ", 
                                   " _ __  ", 
                                   "| \'_ \\ ", 
                                   "| |_) |", 
                                   "| .__/ ", 
                                   "| |    ", 
                                   "|_|    "]
                           'q' -> ["       ", 
                                   "       ", 
                                   "  __ _ ", 
                                   " / _` |", 
                                   "| (_| |", 
                                   " \\__, |", 
                                   "    | |", 
                                   "    |_|"]
                           'r' -> ["      ", 
                                   "      ", 
                                   " _ __ ", 
                                   "| \'__|", 
                                   "| |   ", 
                                   "|_|   ", 
                                   "      ", 
                                   "      "]
                           's' -> ["     ", 
                                   "     ", 
                                   " ___ ", 
                                   "/ __|", 
                                   "\\__ \\", 
                                   "|___/", 
                                   "     ", 
                                   "     "]
                           't' -> [" _   ", 
                                   "| |  ", 
                                   "| |_ ", 
                                   "| __|", 
                                   "| |_ ", 
                                   " \\__|", 
                                   "     ", 
                                   "     "]
                           'u' -> ["       ", 
                                   "       ", 
                                   " _   _ ", 
                                   "| | | |", 
                                   "| |_| |", 
                                   " \\__,_|", 
                                   "       ", 
                                   "       "]
                           'v' -> ["       ", 
                                   "       ", 
                                   "__   __", 
                                   "\\ \\ / /", 
                                   " \\ V / ", 
                                   "  \\_/  ", 
                                   "       ", 
                                   "       "]
                           'w' -> ["          ", 
                                   "          ", 
                                   "__      __", 
                                   "\\ \\ /\\ / /", 
                                   " \\ V  V / ", 
                                   "  \\_/\\_/  ", 
                                   "          ", 
                                   "          "]
                           'x' -> ["      ", 
                                   "      ", 
                                   "__  __", 
                                   "\\ \\/ /", 
                                   " >  < ", 
                                   "/_/\\_\\", 
                                   "      ", 
                                   "      "]
                           'y' -> ["       ", 
                                   "       ", 
                                   " _   _ ", 
                                   "| | | |", 
                                   "| |_| |", 
                                   " \\__, |", 
                                   "  __/ |", 
                                   " |___/ "]
                           'z' -> ["     ", 
                                   "     ", 
                                   " ____", 
                                   "|_  /", 
                                   " / / ", 
                                   "/___|", 
                                   "     ", 
                                   "     "]
                           '{' -> ["   __", 
                                   "  / /", 
                                   " | | ", 
                                   "/ /  ", 
                                   "\\ \\  ", 
                                   " | | ", 
                                   "  \\_\\", 
                                   "     "]
                           '|' -> [" _ ", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "| |", 
                                   "|_|"]
                           '}' -> ["__   ", 
                                   "\\ \\  ", 
                                   " | | ", 
                                   "  \\ \\", 
                                   "  / /", 
                                   " | | ", 
                                   "/_/  ", 
                                   "     "]
                           '~' -> [" /\\/|", 
                                   "|/\\/ ", 
                                   "     ", 
                                   "     ", 
                                   "     ", 
                                   "     ", 
                                   "     ", 
                                   "     "]
                           otherwise -> [" ", 
                                   " ", 
                                   " ", 
                                   " ", 
                                   " ", 
                                   " ", 
                                   " ", 
                                   " "]