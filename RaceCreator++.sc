SCRIPT_START
{
WAIT 0
NOP
LVAR_INT isnew i filename isTrue scplayer almacen offs testcar g esfera char car ich menu selected filen checkp blip coords list
LVAR_FLOAT x y z radio angle x2 y2 z2 
LVAR_TEXT_LABEL16 idd
radio=10.0
GET_PLAYER_CHAR 0 scplayer
GET_CAR_CHAR_IS_USING scplayer car

IF NOT DOES_FILE_EXIST "CLEO/Race Creator++/General settings.ini"
    PRINT_FORMATTED "~r~Error: ~w~CLEO/Race Creator++/General settings.ini not found." 4000
    WAIT 5000
    TERMINATE_THIS_CUSTOM_SCRIPT
ENDIF

GOTO mainmenu
testcar=CAR

mainmenu:
WAIT 100
GET_LABEL_POINTER Coords selected
READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "PlayMarker" selected
SCAN_STRING $selected "%f %f %f" offs x y z

selected=0
READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "BlipID" selected

IF NOT selected=0
    ADD_SHORT_RANGE_SPRITE_BLIP_FOR_COORD x y z selected blip
ENDIF

READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "CheatToOpenMenu" idd
GOTO mainmenutrue



mainmenutrue:
    WAIT 0
    

    WHILE NOT IS_ON_MISSION
    AND NOT IS_ON_CUTSCENE
    AND NOT IS_ON_SCRIPTED_CUTSCENE
        WAIT 0
        IF TEST_CHEAT "rel"
            STREAM_CUSTOM_SCRIPT "RaceCreator++.cs"
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
        
        IF TEST_CHEAT $idd 
            REMOVE_BLIP blip
            GET_PLAYER_CHAR 0 scplayer
            SET_PLAYER_CONTROL 0 0
            GOSUB checkplaneini
            CREATE_MENU RMFTITT (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
            SET_MENU_COLUMN menu 0 DUMMY (RMFCREA RMFEDIT RMFSTAR RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
            WHILE TRUE
                WAIT 0
                IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                    DELETE_MENU menu
                    SET_PLAYER_CONTROL 0 1
                    GOSUB checkplane2ini
                    GOTO mainmenu
                ENDIF
                IF IS_SELECT_MENU_JUST_PRESSED
                OR IS_KEY_JUST_PRESSED VK_SPACE
                    GET_MENU_ITEM_SELECTED menu (selected)
                    SWITCH selected
                        CASE 0
                            DELETE_MENU menu
                            isnew=1
                            GOTO createsection
                            BREAK
                        CASE 1
                            //GET_CHAR_AREA_VISIBLE scplayer offs
                            //IF offs=0
                                i=0
                                GET_LABEL_POINTER Dumper g
                                IF FIND_FIRST_FILE "Cleo/Race Creator++/Races/*.ini" i g
                                    DELETE_MENU menu 
                                    isnew=0
                                    ich=1
                                    GOTO findallfiles
                                ELSE
                                    GET_AUDIO_SFX_VOLUME angle
                                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                                    PRINT_FORMATTED "You have not created any race yet!" 1000
                                ENDIF
                            //ELSE
                                //PRINT_FORMATTED_NOW "~r~You need to be outside to edit a race" 1000  
                            //ENDIF
                            BREAK
                        CASE 2 
                            //GET_CHAR_AREA_VISIBLE scplayer offs
                            //IF offs=0
                                //IF IS_CHAR_IN_ANY_CAR scplayer
                                    i=0
                                    GET_LABEL_POINTER Dumper g
                                    IF FIND_FIRST_FILE "Cleo/Race Creator++/Races/*.ini" i g
                                        DELETE_MENU menu 
                                        isnew=0
                                        ich=0
                                        GOTO findallfiles
                                    ELSE
                                        GET_AUDIO_SFX_VOLUME angle
                                        CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                                        PRINT_FORMATTED "You have not created any race yet!" 1000
                                    ENDIF
                                //ELSE
                                  //  GET_AUDIO_SFX_VOLUME angle
                                    //CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                                    //PRINT_FORMATTED_NOW "You need a vehicle!" 1000         
                                //ENDIF
                            //ELSE
                                //PRINT_FORMATTED_NOW "~r~You need to be outside to start a race" 1000
                            //ENDIF
                            BREAK 
                        CASE 3
                            DELETE_MENU menu
                            SET_PLAYER_CONTROL 0 1
                            GOSUB checkplane2ini
                            GOTO mainmenu
                            BREAK               
                    ENDSWITCH
                ENDIF
            ENDWHILE
        ENDIF
        IF LOCATE_CAMERA_DISTANCE_TO_COORDINATES x y z 200.0
            IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D scplayer x y z 3.0 3.0 3.0 1
                REMOVE_BLIP blip
                SET_PLAYER_CONTROL 0 0
                i=0
                GET_LABEL_POINTER Dumper g
                IF FIND_FIRST_FILE "Cleo/Race Creator++/Races/*.ini" i g
                    DELETE_MENU menu 
                    isnew=0
                    ich=0
                    GOTO findallfiles
                ELSE
                    GET_AUDIO_SFX_VOLUME angle
                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                    PRINT_FORMATTED "You have not created any race yet!" 1000
                    WAIT 1000
                ENDIF
            ENDIF
        ENDIF
    ENDWHILE
goto mainmenutrue
findallfiles:
    //
    
    GOSUB createlabels
    
    IF isnew=1
        FIND_FIRST_FILE "Cleo/Race Creator++/Races/*.ini" i g  //26
    ENDIF
    almacen=0
    IF isnew=0
        GET_LABEL_POINTER Files filen
        STRING_FORMAT filen "%s" $g
        ADD_TEXT_LABEL RMFFME1 $filen
        almacen+=1
    ENDIF
    GET_LABEL_POINTER Files filen
    
    WHILE FIND_NEXT_FILE i g
        WAIT 0
        almacen+=1
        GET_LABEL_POINTER Files filen
        STRING_FORMAT filen "%s" $g
        SWITCH almacen
            CASE 1
                ADD_TEXT_LABEL RMFFME1 $filen
                BREAK
            CASE 2
                ADD_TEXT_LABEL RMFFME2 $filen
                BREAK
            CASE 3
                ADD_TEXT_LABEL RMFFME3 $filen
                BREAK
            CASE 4
                ADD_TEXT_LABEL RMFFME4 $filen
                BREAK
            CASE 5
                ADD_TEXT_LABEL RMFFME5 $filen
                BREAK
            CASE 6
                ADD_TEXT_LABEL RMFFME6 $filen
                BREAK
            CASE 7
                ADD_TEXT_LABEL RMFFME7 $filen
                BREAK
            CASE 8
                ADD_TEXT_LABEL RMFFME8 $filen
                BREAK
            CASE 9
                ADD_TEXT_LABEL RMFFME9 $filen
                BREAK
            CASE 10
                ADD_TEXT_LABEL RMFFME0 $filen
                BREAK
        ENDSWITCH
        GOTO menufiles
        IF almacen=10
            GOTO menufiles
        ENDIF
    ENDWHILE
    GOTO menufiles
//
menufiles:

    // CREATE_MENU RMFFFII (30.0 70.0) (180.0) 1 TRUE TRUE 0 (menu)
    // SET_MENU_COLUMN menu 0 DUMMY (RMFFME1 RMFFME2 RMFFME3 RMFFME4 RMFFME5 RMFFME6 RMFFME7 RMFFME8 RMFFME9 RMFFME0 RCFFNEX RCFEXIT)
    //FIND_CLOSE i
    i=0
    esfera=0
    
    GOSUB setactivecolumns
    GOSUB removelabels
    GOTO realfindallfiles
    
    
    WHILE TRUE
        WAIT 0
        IF IS_SELECT_MENU_JUST_PRESSED
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 0
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME1 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                    AND NOT offs=0
                        DELETE_MENU menu
                        GOSUB removelabels
                        isnew=0
                        IF ich=1
                            GOTO createsection
                        ELSE
                            esfera=2
                            GET_LABEL_POINTER RaceName coords
                            STRING_FORMAT coords "%s" filen
                            GET_LABEL_POINTER Opponentsch coords
                            WRITE_MEMORY coords 4 0 0
                            GOSUB setdefaultopts
                            GOTO racermenu
                        ENDIF
                    ELSE
                        IF ich=1
                            DELETE_MENU menu
                            GOSUB removelabels
                            isnew=0
                            GOTO createsection
                        ELSE
                            PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                        ENDIF
                    ENDIF
                    BREAK
                CASE 1
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME2 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen



                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0

                                GOSUB setdefaultopts

                                
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 2
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME3 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 3
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME4 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 4
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME5 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 5
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME6 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 6
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME7 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 7
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME8 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 8
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME9 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK
                CASE 9
                    GET_LABEL_POINTER Coords filen
                    GET_TEXT_LABEL_STRING RMFFME0 filen
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
                    IF DOES_FILE_EXIST $filename
                        IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
                        AND NOT offs=0
                            DELETE_MENU menu
                            GOSUB removelabels
                            //PRINT_FORMATTED_NOW "%s" 1000 $g
                            isnew=0
                            IF ich=1
                                GOTO createsection
                            ELSE
                                esfera=2
                                GET_LABEL_POINTER RaceName coords
                                STRING_FORMAT coords "%s" filen
                                GET_LABEL_POINTER Opponentsch coords
                                WRITE_MEMORY coords 4 0 0
                                GOSUB setdefaultopts
                                GOTO racermenu
                            ENDIF
                        ELSE
                            IF ich=1
                                DELETE_MENU menu
                                GOSUB removelabels
                                isnew=0
                                GOTO createsection
                            ELSE
                                PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                            ENDIF
                        ENDIF
                    ENDIF
                    BREAK    
                CASE 10
                    IF almacen=10
                    AND FIND_NEXT_FILE i g
                        DELETE_MENU menu
                        isnew=0
                        GOTO findallfiles
                    ENDIF
                    BREAK
                CASE 11
                    DELETE_MENU menu
                    SET_PLAYER_CONTROL 0 1
                    GOSUB removelabels
                    GOTO mainmenu
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE
    PRINT_FORMATTED_NOW "errorrr" 1000
    WAIT 500
    GOTO menufiles

realfindallfiles:
    CREATE_LIST DATATYPE_INT list

    ALLOCATE_MEMORY 127 esfera
    LIST_ADD list esfera
    
    FIND_FIRST_FILE "Cleo/Race Creator++/Races/*.ini" i esfera  //26
    
    isTrue=1
    WHILE isTrue=1
        WAIT 0
        ALLOCATE_MEMORY 127 esfera
        IF FIND_NEXT_FILE i esfera
            LIST_ADD list esfera
        ELSE
            isTrue=0
        ENDIF
    ENDWHILE

    FIND_CLOSE i
    GOTO realmenufiles
//
realmenufiles:
    
    LOAD_TEXTURE_DICTIONARY RCPLUS
    LOAD_SPRITE 1 RC-Z-MA
    USE_TEXT_COMMANDS 1
    SET_TEXT_DRAW_BEFORE_FADE 1
    
    selected=0
    GET_LIST_SIZE list offs
    LOAD_SPRITE 2 "car_logo"

    GET_LABEL_POINTER Coords2 g
    GET_LIST_VALUE_BY_INDEX list selected filen
    STRING_FORMAT g "Cleo/Race Creator++/Races/%s" $filen

    IF READ_STRING_FROM_INI_FILE $g "Settings" "ImageName" idd
    AND NOT IS_STRING_EQUAL $idd "-1" 5 0 "z" 
        x=150.0
        y=150.0
        LOAD_SPRITE 1 $idd
    ELSE    
        READ_INT_FROM_INI_FILE $g "Settings" "IsCircuit" offs
        x=100.0
        y=100.0
        IF offs=0
            LOAD_SPRITE 1 "sprint_logo"
        ELSE
            LOAD_SPRITE 1 "circuit_logo"
        ENDIF
    ENDIF

    IF READ_STRING_FROM_INI_FILE $g "Settings" "Race type" idd
        IF IS_STRING_EQUAL $idd "STREET" 10 0 "z"
            LOAD_SPRITE 2 "car_logo"
        ENDIF
        IF IS_STRING_EQUAL $idd "AIR" 10 0 "z"
            LOAD_SPRITE 2 "plane_logo"
        ENDIF
        IF IS_STRING_EQUAL $idd "SEA" 10 0 "z"
            LOAD_SPRITE 2 "boat_logo"
        ENDIF
    ELSE
        LOAD_SPRITE 2 "car_logo"
    ENDIF
    
    esfera=0
    WHILE TRUE
        WAIT 0
        
        IF IS_KEY_JUST_PRESSED VK_RIGHT
        OR IS_KEY_JUST_PRESSED VK_KEY_D

            
            GET_LIST_SIZE list offs
            IF IS_KEY_PRESSED VK_LSHIFT
                selected+=5
                IF NOT selected<offs
                    selected=offs
                    selected-=1
                    GET_AUDIO_SFX_VOLUME angle
                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                ENDIF
            ELSE
                selected+=1
            ENDIF

            IF selected<offs
                x=150.0
                y=150.0
                GET_LABEL_POINTER Coords filen
                GET_LABEL_POINTER Coords2 g
                GET_LIST_VALUE_BY_INDEX list selected filen
                STRING_FORMAT g "Cleo/Race Creator++/Races/%s" $filen
                IF READ_STRING_FROM_INI_FILE $g "Settings" "ImageName" idd
                AND NOT IS_STRING_EQUAL $idd "-1" 5 0 "z" 
                    LOAD_SPRITE 1 $idd
                ELSE    
                    READ_INT_FROM_INI_FILE $g "Settings" "IsCircuit" offs
                    x=100.0
                    y=100.0
                    IF offs=0
                        LOAD_SPRITE 1 "sprint_logo"
                    ELSE
                        LOAD_SPRITE 1 "circuit_logo"
                    ENDIF
                ENDIF
                IF READ_STRING_FROM_INI_FILE $g "Settings" "Race type" idd
                    IF IS_STRING_EQUAL $idd "STREET" 10 0 "z"
                        LOAD_SPRITE 2 "car_logo"
                    ENDIF
                    IF IS_STRING_EQUAL $idd "AIR" 10 0 "z"
                        LOAD_SPRITE 2 "plane_logo"
                    ENDIF
                    IF IS_STRING_EQUAL $idd "SEA" 10 0 "z"
                        LOAD_SPRITE 2 "boat_logo"
                    ENDIF
                ELSE
                    LOAD_SPRITE 2 "car_logo"
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ELSE
                selected-=1
                GET_AUDIO_SFX_VOLUME angle
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
            ENDIF  
        ENDIF 

        IF IS_KEY_JUST_PRESSED VK_LEFT
        OR IS_KEY_JUST_PRESSED VK_KEY_A

            GET_LIST_SIZE list offs
            IF IS_KEY_PRESSED VK_LSHIFT
                selected-=5
                IF selected<0
                    selected=0
                    GET_AUDIO_SFX_VOLUME angle
                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                ENDIF
            ELSE
                selected-=1
            ENDIF

            IF selected>-1
                x=150.0
                y=150.0
                GET_LABEL_POINTER Coords filen
                GET_LABEL_POINTER Coords2 g
                GET_LIST_VALUE_BY_INDEX list selected filen
                STRING_FORMAT g "Cleo/Race Creator++/Races/%s" $filen
                IF READ_STRING_FROM_INI_FILE $g "Settings" "ImageName" idd
                AND NOT IS_STRING_EQUAL $idd "-1" 5 0 "z" 
                    LOAD_SPRITE 1 $idd
                ELSE    
                    READ_INT_FROM_INI_FILE $g "Settings" "IsCircuit" offs
                    x=100.0
                    y=100.0
                    IF offs=0
                        LOAD_SPRITE 1 "sprint_logo"
                    ELSE
                        LOAD_SPRITE 1 "circuit_logo"
                    ENDIF
                ENDIF
                IF READ_STRING_FROM_INI_FILE $g "Settings" "Race type" idd
                    IF IS_STRING_EQUAL $idd "STREET" 10 0 "z"
                        LOAD_SPRITE 2 "car_logo"
                    ENDIF
                    IF IS_STRING_EQUAL $idd "AIR" 10 0 "z"
                        LOAD_SPRITE 2 "plane_logo"
                    ENDIF
                    IF IS_STRING_EQUAL $idd "SEA" 10 0 "z"
                        LOAD_SPRITE 2 "boat_logo"
                    ENDIF
                ELSE
                    LOAD_SPRITE 2 "car_logo"
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ELSE
                selected+=1
                GET_AUDIO_SFX_VOLUME angle
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
            ENDIF   
        ENDIF 

        IF ich=0
            IF IS_KEY_JUST_PRESSED VK_UP
            OR IS_KEY_JUST_PRESSED VK_KEY_W
            OR IS_KEY_JUST_PRESSED VK_KEY_S
            OR IS_KEY_JUST_PRESSED VK_DOWN
                IF esfera=0
                    esfera=1
                ELSE
                    esfera=0
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ENDIF
            IF esfera=0
                DRAW_STRING_EXT "Race" DRAW_EVENT_BEFORE_HUD 325.0 320.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 0 1 0 0 0 200 1 170 90 90 200
                DRAW_STRING_EXT "Time trial" DRAW_EVENT_BEFORE_HUD 325.0 345.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 150 0 1 0 0 0 200 1 0 0 0 150
            ELSE
                DRAW_STRING_EXT "Race" DRAW_EVENT_BEFORE_HUD 325.0 320.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 150 0 1 0 0 0 200 1 0 0 0 150
                DRAW_STRING_EXT "Time trial" DRAW_EVENT_BEFORE_HUD 325.0 345.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 0 1 0 0 0 200 1 170 90 90 200
            ENDIF
        ENDIF

        DRAW_STRING_EXT $filen DRAW_EVENT_BEFORE_HUD 325.0 80.0 0.2 0.8 0 2 1 0 220.0 1 255 255 255 255 0 1 0 0 0 200 1 0 0 0 200
        DRAW_RECT 325.0 78.0 225.0 2.5 150 150 150 255
        DRAW_RECT 325.0 98.0 225.0 2.5 150 150 150 255

        DRAW_RECT 325.0 215.0 180.0 180.0 0 0 0 180
        DRAW_RECT 325.0 126.0 180.0 5.0 150 150 150 255
        DRAW_RECT 325.0 304.0 180.0 5.0 150 150 150 255

        

        DRAW_SPRITE 1 325.0 215.0 x y 255 255 255 255
        DRAW_SPRITE 2 438.0 290.0 30.0 30.0 255 255 255 255

        //help

        //DRAW_RECT 557.0 410.0 120.0 60.0 0 0 50 50
        DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 369.5 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~WASD / Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 384.7 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Shift + arrow~w~: Fast navigation" DRAW_EVENT_BEFORE_HUD 555.0 400.0 0.12 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 415.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 430.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200
        //hacer iconos de tipo de auto, dinero, etc?
        
        IF IS_KEY_JUST_PRESSED VK_EXECUTE
        OR IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
            WAIT 0
            REMOVE_TEXTURE_DICTIONARY
            FREE_MEMORY list
            DELETE_LIST list
            USE_TEXT_COMMANDS 0
            SET_TEXT_DRAW_BEFORE_FADE 0
            FREE_MEMORY g
            SET_PLAYER_CONTROL 0 1
            GOTO mainmenu
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_SPACE    
            WAIT 0
            GET_LABEL_POINTER Dumper filename     
            STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen

            IF READ_INT_FROM_INI_FILE $filename "Checkpoints" "1" offs
            AND NOT offs=0
                
                REMOVE_TEXTURE_DICTIONARY
                FREE_MEMORY list
                RESET_LIST list
                DELETE_LIST list
                USE_TEXT_COMMANDS 0
                SET_TEXT_DRAW_BEFORE_FADE 0
                FREE_MEMORY g

                isnew=0
                IF ich=1
                    esfera=0
                    GOTO createsection
                ELSE
                    isnew=esfera
                    esfera=2
                    GET_LABEL_POINTER RaceName coords
                    STRING_FORMAT coords "%s" filen
                    GET_LABEL_POINTER Opponentsch coords
                    WRITE_MEMORY coords 4 0 0
                    GOSUB setdefaultopts
                    GOTO racermenu
                ENDIF
            ELSE
                IF ich=1
                REMOVE_TEXTURE_DICTIONARY
                FREE_MEMORY list
                RESET_LIST list
                DELETE_LIST list
                USE_TEXT_COMMANDS 0
                SET_TEXT_DRAW_BEFORE_FADE 0
                FREE_MEMORY g
                    isnew=0
                    GOTO createsection
                ELSE
                    PRINT_FORMATTED_NOW "~r~This race doesnt have enough checkpoints!" 1000
                ENDIF
            ENDIF
        ENDIF
    ENDWHILE
    PRINT_FORMATTED_NOW "errorrr" 1000
    WAIT 500
    GOTO menufiles

//
createlabels:
    ADD_TEXT_LABEL RMFFME1 "-"
    ADD_TEXT_LABEL RMFFME2 "-"
    ADD_TEXT_LABEL RMFFME3 "-"
    ADD_TEXT_LABEL RMFFME4 "-"
    ADD_TEXT_LABEL RMFFME5 "-"
    ADD_TEXT_LABEL RMFFME6 "-"
    ADD_TEXT_LABEL RMFFME7 "-"
    ADD_TEXT_LABEL RMFFME8 "-"
    ADD_TEXT_LABEL RMFFME9 "-"
    ADD_TEXT_LABEL RMFFME0 "-"
RETURN
removelabels:
	REMOVE_TEXT_LABEL RMFFME1
	REMOVE_TEXT_LABEL RMFFME2
	REMOVE_TEXT_LABEL RMFFME3
	REMOVE_TEXT_LABEL RMFFME4
	REMOVE_TEXT_LABEL RMFFME5
	REMOVE_TEXT_LABEL RMFFME6
	REMOVE_TEXT_LABEL RMFFME7
	REMOVE_TEXT_LABEL RMFFME8
	REMOVE_TEXT_LABEL RMFFME9
	REMOVE_TEXT_LABEL RMFFME0
	RETURN
//
createsection:
    CLEO_CALL createPreps 0 isnew scplayer filename
    GOTO mainmenu 
//
//checkplane
    checkplaneini:
        IF IS_CHAR_IN_ANY_CAR scplayer
            GET_CAR_CHAR_IS_USING scplayer car
            GET_VEHICLE_SUBCLASS car selected
            IF selected=VEHICLE_SUBCLASS_PLANE
            OR selected=VEHICLE_SUBCLASS_FPLANE
                FREEZE_CAR_POSITION car 1
            ENDIF
        ENDIF
        RETURN
    //
    checkplane2ini:
        IF IS_CHAR_IN_ANY_CAR scplayer
            GET_CAR_CHAR_IS_USING scplayer car
            FREEZE_CAR_POSITION car 0
        ENDIF
        RETURN
//


//
//
updatech:

    IF NOT SCAN_STRING $almacen "%f %f %f %f %i %f %i" offs x y z radio char angle blip
        STRING_FORMAT almacen "%f %f %f %f %i %f %i" x y z radio char angle 1
        WRITE_STRING_TO_INI_FILE $almacen $filename "Checkpoints" $coords
    ENDIF
    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $coords almacen
    SCAN_STRING $almacen "%f %f %f %f %i %f %i" offs x y z radio char angle blip
    STRING_FORMAT almacen "%f %f %f %f %i %f %i %.2f %.1f" x y z radio char angle blip 1.0 0.0
    WRITE_STRING_TO_INI_FILE $almacen $filename "Checkpoints" $coords
    RETURN 
//
setdefaultopts:
    GET_LABEL_POINTER RaceOpts coords
    WRITE_MEMORY coords 4 0 0
    coords+=4
    WRITE_MEMORY coords 4 0 0
    coords+=4
    WRITE_MEMORY coords 4 0 0
    coords+=4
    WRITE_MEMORY coords 4 0 0
    coords+=4
    IF READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "VisualDamage" offs
        IF offs=1
            WRITE_MEMORY coords 4 1 0
        ELSE
            WRITE_MEMORY coords 4 0 0
        ENDIF
    ELSE
        WRITE_MEMORY coords 4 1 0
    ENDIF
    coords+=4
    WRITE_MEMORY coords 4 0 0
    coords+=4
    WRITE_MEMORY coords 4 0 0
    coords+=4
    IF READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "MechDamage" offs
        IF offs=1
            WRITE_MEMORY coords 4 1 0
        ELSE
            WRITE_MEMORY coords 4 0 0
        ENDIF
    ELSE
        WRITE_MEMORY coords 4 1 0
    ENDIF
    coords+=4
    WRITE_MEMORY coords 4 0 0
RETURN
//
racermenu:

    SET_PLAYER_CONTROL 0 0
    GET_LABEL_POINTER IsLive coords
    WRITE_MEMORY coords 4 0 0
    READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
    
    // IF offs=0
    //     ACTIVATE_MENU_ITEM menu 3 0
    // ENDIF

    GET_LABEL_POINTER Coords almacen

    IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" almacen
    AND NOT IS_STRING_EQUAL $almacen "DELETED" 8 0 "z"
        SCAN_STRING $almacen "%f %f %f %f %i %f %i" offs x y z radio char angle blip
        IF NOT blip=1
            STRING_FORMAT almacen "%f %f %f %f %i %f %i" x y z radio char angle 1
            WRITE_STRING_TO_INI_FILE $almacen $filename "Checkpoints" "0"
        ENDIF
        READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" almacen
        IF NOT SCAN_STRING $almacen "%f %f %f %f %i %f %i %f %f" offs x y z radio char angle blip y2 z2
            STRING_FORMAT almacen "%f %f %f %f %i %f %i %.2f %.1f" x y z radio char angle blip 1.0 0.0
            WRITE_STRING_TO_INI_FILE $almacen $filename "Checkpoints" "0"
        ENDIF
    ENDIF

    GET_LABEL_POINTER Opponentsch coords
    READ_MEMORY coords 4 0 selected
    IF NOT READ_FLOAT_FROM_INI_FILE $filename "Settings" "ModVersion" z2
        z2=0.0
    ENDIF

    IF selected=0
        IF z2=2.0
            isTrue=10
        ELSE
            isTrue=1
        ENDIF
        selected+=1
        WHILE isTrue>0
            WAIT 0
            PRINT_FORMATTED_NOW "~y~Loading..." 100
            GET_LABEL_POINTER Istring coords
            STRING_FORMAT coords "%i" selected
            GET_LABEL_POINTER Coords almacen
            
            IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $coords almacen
            AND NOT IS_STRING_EQUAL $almacen "DELETED" 8 0 "z"
                IF NOT SCAN_STRING $almacen "%f %f %f %f %i %f %i %f %f" offs x y z radio char angle blip x2 z2
                    GOSUB updatech
                    isTrue=1
                ENDIF
                selected+=isTrue
            ELSE
                selected-=1
                STRING_FORMAT coords "%i" selected
                WHILE NOT READ_STRING_FROM_INI_FILE $filename "Checkpoints" $coords almacen
                OR IS_STRING_EQUAL $almacen "DELETED" 8 0 "z"
                    WAIT 0  
                    selected-=1
                    STRING_FORMAT coords "%i" selected
                ENDWHILE
                STRING_FORMAT coords "%i" selected
                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $coords almacen
                SCAN_STRING $almacen "%f %f %f %f %i %f %i %f %f" offs x y z radio char angle blip x2 y2

                IF NOT blip>0
                    STRING_FORMAT almacen "%f %f %f %f %i %f %i %f %f" x y z radio char angle 1 x2 y2
                    WRITE_STRING_TO_INI_FILE $almacen $filename "Checkpoints" $coords
                ENDIF

                
                GET_LABEL_POINTER TotCheck coords
                WRITE_MEMORY coords 4 selected 0
                READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
                IF offs=1
                    selected+=1
                ENDIF
                GET_LABEL_POINTER Opponentsch coords
                WRITE_MEMORY coords 4 selected 0
                GET_LABEL_POINTER Auxiliar coords
                coords+=8
                WRITE_MEMORY coords 4 selected 0
                IF isTrue=1
                    WRITE_FLOAT_TO_INI_FILE 2.0 $filename "Settings" "ModVersion"
                ENDIF
                isTrue=0
            ENDIF
        ENDWHILE
    ENDIF
    //sacar nombre
    LOAD_TEXTURE_DICTIONARY RCPLUS

    GET_LABEL_POINTER RaceName coords
    GET_LABEL_POINTER Coords selected
    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" selected
    IF IS_STRING_EQUAL $selected "AIR" 5 0 "z"
        LOAD_SPRITE 2 "plane_logo"
        STRING_FORMAT selected "Air race"
    ELSE
        IF IS_STRING_EQUAL $selected "SEA" 5 0 "z"
            LOAD_SPRITE 2 "boat_logo"
            STRING_FORMAT selected "Sea race"
        ELSE
            LOAD_SPRITE 2 "car_logo"
            STRING_FORMAT selected "Street race"
        ENDIF
    ENDIF

    STRING_FORMAT idd "%s" $selected
    ADD_TEXT_LABEL RMFFME1 $idd
    
    READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
    IF offs=1
        STRING_FORMAT idd "Circuit"
        ADD_TEXT_LABEL RMFFME2 $idd
        LOAD_SPRITE 1 "circuit_logo_s"
    ELSE
        STRING_FORMAT idd "Sprint"
        ADD_TEXT_LABEL RMFFME2 $idd
        LOAD_SPRITE 1 "sprint_logo_s"
    ENDIF

    ADD_TEXT_LABEL RMFFME0 $coords
    GET_LABEL_POINTER Auxiliar coords
    coords+=8
    READ_MEMORY coords 4 0 selected
    GET_LABEL_POINTER Coords coords
    STRING_FORMAT coords "%i checkpoints" selected
    ADD_TEXT_LABEL RMFFME3 $coords
    SET_TEXT_DRAW_BEFORE_FADE 1
    USE_TEXT_COMMANDS 1
    //CREATE_MENU RCFOPTI (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
    //SET_MENU_COLUMN menu 0 DUMMY (RMFSTAR RCFRCUP RCFRCUT RCFLASS RCFRCSE RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
    selected=2
    STRING_FORMAT idd "Laps:%i" esfera
    ADD_TEXT_LABEL RMFFME4 $idd
    ADD_TEXT_LABEL RMFFME5 "+"
    ADD_TEXT_LABEL RMFFME6 "-"

    IF NOT isnew=0
        selected=0
    ENDIF

    WHILE TRUE
        WAIT 0
        
        //
        DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 115.0 70.0 170.0 100.0 0.0 0.0 0 0 0 0 25 70 200
                    
        DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 115.0 220.0 170.0 205.0 0.0 0.0 0 0 0 0 0 0 200

        DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 115.0 20.0 170.0 5.0 0.0 0.0 0 0 0 150 150 150 255
        DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 115.0 120.0 170.0 5.0 0.0 0.0 0 0 0 150 150 150 255//bordes

        DRAW_SPRITE 2 53.0 86.0 23.1 23.1 255 255 255 255 //auto
        DRAW_SPRITE 1 50.3 107.0 19.0 20.0 255 255 255 255 //circuito
        
        DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 355.5 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~WASD / Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 380.55 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~O and P~w~: Change tunes/paintjobs" DRAW_EVENT_BEFORE_HUD 555.0 400.0 0.12 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 415.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 430.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200
        
        // SET_TEXT_FONT 2
        // SET_TEXT_SCALE 0.39 1.6
        // DISPLAY_TEXT 55.0 30.0 RMFFME0 //racename
        GET_LABEL_POINTER Coords coords
        GET_TEXT_LABEL_STRING RMFFME0 coords
        DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 117.0 30.0 0.39 0.78 0 2 1 0 160.0 1 255 255 255 255 1 1 0 0 0 200 0 170 90 90 200 //racename
        
        // SET_TEXT_EDGE 1 0 0 50 255
        // SET_TEXT_SCALE 0.2 1.3
        // DISPLAY_TEXT 55.0 67.0 RMFFME1 //streetseaair
        GET_TEXT_LABEL_STRING RMFFME1 coords
        DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 82.0 85.0 0.2 0.6 0 1 1 0 160.0 1 255 255 255 255 1 1 0 0 50 255 0 170 90 90 200 //street
        
        // SET_TEXT_EDGE 1 0 0 50 255
        // SET_TEXT_SCALE 0.2 1.2
        // DISPLAY_TEXT 105.0 67.0 RMFFME2 //circuit sprint
        GET_TEXT_LABEL_STRING RMFFME2 coords
        DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 73.7 100.0 0.2 0.6 0 1 1 0 160.0 1 255 255 255 255 1 1 0 0 50 255 0 170 90 90 200 //circuit
        
        // SET_TEXT_EDGE 1 0 0 50 255
        // SET_TEXT_SCALE 0.2 1.3
        // DISPLAY_TEXT 115.0 83.0 RMFFME3 //checkpoints
        GET_TEXT_LABEL_STRING RMFFME3 coords
        DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 125.0 100.0 0.2 0.6 0 1 1 0 160.0 1 255 255 255 255 1 1 0 0 50 255 0 170 90 90 200
        
        IF isnew=0
            //laps
            IF offs=1
                DRAW_RECT 66.0 145.0 60.0 25.0 0 25 70 200 //laps
                DRAW_RECT 105.0 135.0 15.0 15.0 0 25 70 200 //+
                DRAW_RECT 105.0 155.0 15.0 15.0 0 25 70 200 //-

                SET_TEXT_SCALE 0.4 1.5
                SET_TEXT_EDGE 1 0 0 0 255
                DISPLAY_TEXT 38.0 140.0 RMFFME4

                //+
                IF selected=0
                    SET_TEXT_COLOUR 255 255 150 240
                ELSE
                    SET_TEXT_COLOUR 255 255 255 130
                ENDIF

                SET_TEXT_SCALE 0.4 1.5
                SET_TEXT_EDGE 1 0 0 0 255
                DISPLAY_TEXT 101.0 128.0 RMFFME5

                //-
                IF selected=1
                    SET_TEXT_COLOUR 255 255 150 240
                ELSE
                    SET_TEXT_COLOUR 255 255 255 130
                ENDIF

                SET_TEXT_SCALE 0.4 1.5
                SET_TEXT_EDGE 1 0 0 0 255
                DISPLAY_TEXT 102.0 148.0 RMFFME6 
            ENDIF
            
            //start race
                
                GET_TEXT_LABEL_STRING RMFSTAR coords
                IF selected=2
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 175.0 0.4 0.8 0 1 1 0 130.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 175.0 0.4 0.8 0 1 1 0 130.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
                ENDIF
            //start onemake race
                
                GET_TEXT_LABEL_STRING RCFRCUT coords
                IF selected=3
                    
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "TunesForOnemakeRaces" coords
                    IF coords=1
                        DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                    ELSE
                        DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                    ENDIF
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "PaintjobsForOnemakeRaces" coords
                    IF coords=1
                        DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                    ELSE
                        DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                    ENDIF
                    
                    IF IS_KEY_JUST_PRESSED VK_KEY_O
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "TunesForOnemakeRaces" coords
                        IF coords=1
                            WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Settings" "TunesForOnemakeRaces"
                        ELSE
                            WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Settings" "TunesForOnemakeRaces"
                        ENDIF 
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_KEY_P
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "PaintjobsForOnemakeRaces" coords
                        IF coords=1
                            WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Settings" "PaintjobsForOnemakeRaces"
                        ELSE
                            WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Settings" "PaintjobsForOnemakeRaces"
                        ENDIF 
                    ENDIF
                    GET_TEXT_LABEL_STRING RCFRCUT coords
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 195.0 0.4 0.8 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 195.00 0.4 0.8 0 1 1 0 160.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
                ENDIF
            //start custom race
                GET_TEXT_LABEL_STRING RCFRCUP coords
                IF selected=4
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 230.0 0.4 0.8 0 1 1 0 130.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 230.00 0.4 0.8 0 1 1 0 130.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
                ENDIF
            //customize
                
                GET_TEXT_LABEL_STRING RCFRCSE coords
                IF selected=5
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 250.0 0.4 0.8 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 250.00 0.4 0.8 0 1 1 0 160.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
                ENDIF
            //settings
            GET_TEXT_LABEL_STRING RCFOPTI coords
            IF selected=6
                DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 285.0 0.4 0.8 0 1 1 0 100.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
            ELSE
                DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 285.00 0.4 0.8 0 1 1 0 100.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
            ENDIF
            //exit
                // DRAW_RECT 52.0 310.0 30.0 15.0 50 0 0 150 //laps
                // IF selected=7
                //     SET_TEXT_COLOUR 220 128 0 240
                // ELSE
                //     SET_TEXT_COLOUR 255 100 100 130
                // ENDIF
                // SET_TEXT_SCALE 0.4 1.2
                // SET_TEXT_EDGE 1 0 0 0 255
                // DISPLAY_TEXT 38.0 305.0 RCFEXIT
                GET_TEXT_LABEL_STRING RCFEXIT coords
                IF selected=7
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 113.5 305.0 0.4 0.8 0 1 1 0 120.0 1 240 128 0 255 1 1 0 0 0 255 0 220 128 0 200
                ELSE
                    DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 113.5 305.00 0.4 0.8 0 1 1 0 50.0 1 255 100 100 130 1 1 0 0 0 255 0 50 50 50 200
                ENDIF
            //GET_LABEL_POINTER MenusHelp coords
            //READ_MEMORY coords 4 0 selected
            IF IS_KEY_JUST_PRESSED VK_DOWN
            OR IS_KEY_JUST_PRESSED VK_RIGHT
                IF selected=7
                    IF offs=0
                        selected=2
                    ELSE
                        selected=0
                    ENDIF
                ELSE
                    selected+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_UP
            OR IS_KEY_JUST_PRESSED VK_LEFT
                IF offs=0
                    IF selected=2
                        selected=7
                    ELSE
                        selected-=1
                    ENDIF
                ELSE
                    IF selected=0
                        selected=7
                    ELSE
                        selected-=1
                    ENDIF
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ENDIF


            IF selected=2
                PRINT_FORMATTED_NOW "You can choose a categorie, all opponents will race with a car of that categorie (CJ too if he is on foot)." 100
            ENDIF
            IF selected=4
                PRINT_FORMATTED_NOW "Read custom settings from the .ini file. You can change them selecting ~y~Customize opponents" 100
            ENDIF
            IF selected=3
                PRINT_FORMATTED_NOW "All opponents will race with the car you are in currently." 100
            ENDIF
            IF selected=5
                PRINT_FORMATTED_NOW "Customize opponents cars. Customized opponents will only appear on ~y~Custom races~w~. Settings will be saved in the ini file" 100
            ENDIF


            IF IS_KEY_JUST_PRESSED VK_SPACE
                GET_AUDIO_SFX_VOLUME angle
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 1
                SWITCH selected
                    CASE 0
                        esfera+=1
                        STRING_FORMAT idd "Laps:%i" esfera
                        ADD_TEXT_LABEL RMFFME4 $idd
                        BREAK
                    CASE 1
                        IF NOT esfera<2
                            esfera-=1
                            STRING_FORMAT idd "Laps:%i" esfera
                            ADD_TEXT_LABEL RMFFME4 $idd
                        ELSE
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 17
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 14
                        ENDIF
                        BREAK
                    CASE 2
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        GOSUB removeracetex
                        IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                            GOTO cateraceair
                        ENDIF
                        IF IS_STRING_EQUAL $idd "SEA" 4 0 "z"
                            GOTO cateracesea
                        ENDIF
                        GOTO caterace
                        BREAK
                    CASE 3
                        IF IS_CHAR_IN_ANY_CAR scplayer
                            isnew=1
                            GOSUB removeracetex
                            CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                            GOTO mainmenu
                        ELSE
                            GET_AUDIO_SFX_VOLUME angle
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                            PRINT_FORMATTED_NOW "~r~You need a vehicle!" 1000 
                            WAIT 500
                        ENDIF
                        BREAK
                    CASE 4
                        WHILE IS_KEY_PRESSED VK_SPACE
                            WAIT 0
                        ENDWHILE
                        IF IS_CHAR_IN_ANY_CAR scplayer
                            isnew=0
                            GOSUB removeracetex
                            CREATE_MENU RCFOPTI (30.0 90.0) (180.0) 1 TRUE TRUE 0 (menu)
                            SET_MENU_COLUMN menu 0 DUMMY (RMFSTAR RCLIV-C RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
                            WHILE TRUE
                                WAIT 0
                                GET_MENU_ITEM_SELECTED menu selected
                                IF selected=1
                                    PRINT_FORMATTED_NOW "Select unique custom cars (such as mission cars), or cars with modded parts (from tuning mod, for example) for each opponent." 50
                                ENDIF
                                IF IS_SELECT_MENU_JUST_PRESSED
                                    GET_MENU_ITEM_SELECTED menu selected
                                    SWITCH selected
                                        CASE 0
                                            DELETE_MENU menu
                                            CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                                            GOTO mainmenu
                                            BREAK
                                        CASE 1
                                            DELETE_MENU menu
                                            GET_LABEL_POINTER IsLive coords
                                            WRITE_MEMORY coords 4 1 0
                                            CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                                            GOTO mainmenu
                                            BREAK
                                        CASE 2
                                            DELETE_MENU menu
                                            GOTO racermenu
                                            BREAK
                                    ENDSWITCH
                                ENDIF
                            ENDWHILE
                            CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                            GOTO mainmenu
                        ELSE
                            GET_AUDIO_SFX_VOLUME angle
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                            PRINT_FORMATTED_NOW "~r~You need a vehicle!" 1000 
                            WAIT 500
                        ENDIF
                        BREAK 
                    CASE 5
                        GOSUB removeracetex
                        GOTO customopp
                        BREAK 
                    CASE 6
                        GOSUB removeracetex
                        SET_TEXT_DRAW_BEFORE_FADE 1
                        CLEO_CALL raceoptions 0 0
                        SET_TEXT_DRAW_BEFORE_FADE 0
                        GOTO racermenu
                        BREAK
                    CASE 7
                        GOSUB removeracetex
                        GOSUB checkplane2ini
                        SET_PLAYER_CONTROL 0 1
                        SET_TEXT_DRAW_BEFORE_FADE 0
                        USE_TEXT_COMMANDS 0
                        REMOVE_TEXTURE_DICTIONARY
                        GOTO mainmenu   
                        BREAK           
                ENDSWITCH
            ENDIF
        ELSE
            GOSUB menuTT
        ENDIF
        IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
            GOSUB removeracetex
            GOSUB checkplane2ini
            SET_PLAYER_CONTROL 0 1
            SET_TEXT_DRAW_BEFORE_FADE 0
            USE_TEXT_COMMANDS 0
            REMOVE_TEXTURE_DICTIONARY
            GOTO mainmenu
        ENDIF  
    ENDWHILE
//
menuTT:
    //start
        GET_TEXT_LABEL_STRING RC+?1ME coords
        IF selected=0
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 160.0 0.4 0.8 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
        ELSE
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 160.0 0.4 0.8 0 1 1 0 160.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
        ENDIF
    //startcate
        GET_TEXT_LABEL_STRING RC+?2ME coords
        IF selected=1
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 190.0 0.4 0.8 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
        ELSE
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 190.0 0.4 0.8 0 1 1 0 160.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
        ENDIF
    //options
        GET_TEXT_LABEL_STRING RCFOPTI coords
        IF selected=2
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 235.0 0.4 0.8 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
        ELSE
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 115.0 235.0 0.4 0.8 0 1 1 0 160.0 1 255 255 255 150 1 1 0 0 0 255 1 50 50 50 200
        ENDIF
    //exit
        GET_TEXT_LABEL_STRING RCFEXIT coords
        IF selected=3
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 113.5 260.0 0.4 0.8 0 1 1 0 120.0 1 240 128 0 255 1 1 0 0 0 255 0 220 128 0 200
        ELSE
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 113.5 260.00 0.4 0.8 0 1 1 0 50.0 1 255 100 100 130 1 1 0 0 0 255 0 50 50 50 200
        ENDIF
    //
        IF IS_KEY_JUST_PRESSED VK_DOWN
        OR IS_KEY_JUST_PRESSED VK_RIGHT
            IF selected=3
                selected=0
            ELSE
                selected+=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_UP
        OR IS_KEY_JUST_PRESSED VK_LEFT
            IF selected=0
                selected=3
            ELSE
                selected-=1
            ENDIF

            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_SPACE
            GET_AUDIO_SFX_VOLUME angle
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 1
            SWITCH selected
                CASE 1
                    isnew=11
                    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                    GOSUB removeracetex
                    IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                        GOTO cateraceair
                    ENDIF
                    IF IS_STRING_EQUAL $idd "SEA" 4 0 "z"
                        GOTO cateracesea
                    ENDIF
                    GOTO caterace
                    BREAK
                CASE 0
                    IF IS_CHAR_IN_ANY_CAR scplayer
                        isnew=10
                        GOSUB removeracetex
                        CREATE_LIST DATATYPE_INT list
                                goto testa
                                DELETE_LIST list
                        GOTO mainmenu
                    ELSE
                        GET_AUDIO_SFX_VOLUME angle
                        CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                        PRINT_FORMATTED_NOW "~r~You need a vehicle!" 1000 
                        WAIT 500
                    ENDIF
                    BREAK
                CASE 2
                    GOSUB removeracetex
                    SET_TEXT_DRAW_BEFORE_FADE 1
                    CLEO_CALL raceoptions 0 0
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    GOTO racermenu
                    BREAK
                CASE 3
                    GOSUB removeracetex
                    GOSUB checkplane2ini
                    SET_PLAYER_CONTROL 0 1
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    USE_TEXT_COMMANDS 0
                    REMOVE_TEXTURE_DICTIONARY
                    GOTO mainmenu   
                    BREAK   
            ENDSWITCH
        ENDIF
        RETURN
    //
removeracetex:
                    USE_TEXT_COMMANDS 0
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    REMOVE_TEXTURE_DICTIONARY
                    REMOVE_TEXT_LABEL RMFFME0
                    REMOVE_TEXT_LABEL RMFFME1
                    REMOVE_TEXT_LABEL RMFFME2
                    REMOVE_TEXT_LABEL RMFFME3
                    REMOVE_TEXT_LABEL RMFFME4
                    REMOVE_TEXT_LABEL RMFFME5
                    REMOVE_TEXT_LABEL RMFFME6
RETURN
//
cateraceair:
    CREATE_MENU RCCATTA (30.0 90.0) (180.0) 1 TRUE TRUE 0 (menu)
    SET_MENU_COLUMN menu 0 DUMMY (RCCPLAP RCCOHEA RCCUSST RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
    WHILE TRUE
        WAIT 0
        IF IS_SELECT_MENU_JUST_PRESSED
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 0
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 11 0
                    isnew=2
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 1
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 12 0
                    isnew=2
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 2
                    DELETE_MENU menu
                    GOSUB createlabels
                    isTrue=1
                    GOTO customracemenu
                    BREAK
                CASE 3
                    DELETE_MENU menu
                    IF isnew=11
                        isnew=1
                    ENDIF
                    GOTO racermenu
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE
//
cateracesea:
    CREATE_MENU RCCATTA (30.0 90.0) (180.0) 1 TRUE TRUE 0 (menu)
    SET_MENU_COLUMN menu 0 DUMMY (RCCSEBO RCCUSST RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
    WHILE TRUE
        WAIT 0
        IF IS_SELECT_MENU_JUST_PRESSED
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 0
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 13 0
                    isnew=2
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 1
                    DELETE_MENU menu
                    GOSUB createlabels
                    isTrue=1
                    GOTO customracemenu
                    BREAK 
                CASE 2
                    DELETE_MENU menu
                    IF isnew=11
                        isnew=1
                    ENDIF
                    GOTO racermenu
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE                  
//
caterace:
    CREATE_MENU RCCATTA (30.0 90.0) (180.0) 1 TRUE TRUE 0 (menu)
    SET_MENU_COLUMN menu 0 DUMMY (RCCATEO RCCATTU RCCATMU RCCATLW RCCATCI RCCATSE RCCATOF RCCATBK RCCARAC RCCAMIX RCCUSST RCFEXIT)
    WHILE TRUE
        WAIT 0

        DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 369.5 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~WASD / Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 384.7 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~O and P~w~: Change tunes/paintjobs" DRAW_EVENT_BEFORE_HUD 555.0 400.0 0.12 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 415.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 430.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200
        

        GET_MENU_ITEM_SELECTED menu offs
        IF offs=9
            PRINT_FORMATTED_NOW "~y~Chaos mode~w~: any car can be spawned!" 100
        ENDIF

        SWITCH offs
            CASE 0
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Exotics" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Exotics" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 1
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Tuners" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Tuners" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 2
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Muscle" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Muscle" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 3
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 4
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Coupes" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Coupes" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 5
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Sedans" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Sedans" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 6
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Offroad" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Offroad" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 7
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Bikes" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Bikes" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 8
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Racecars" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Racecars" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            CASE 9
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Mixed" "AllowTunes" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF

                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Mixed" "AllowPaintjobs" selected
                IF selected=1
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ELSE
                    DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 545.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
                ENDIF
                BREAK
            DEFAULT
                NOP
                BREAK
        ENDSWITCH

        IF IS_KEY_JUST_PRESSED VK_KEY_O
            SWITCH offs
                CASE 0
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Exotics" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Exotics" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Exotics" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 1
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Tuners" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Tuners" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Tuners" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 2
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Muscle" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Muscle" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Muscle" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 3
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 4
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Coupes" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Coupes" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Coupes" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 5
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Sedans" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Sedans" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Sedans" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 6
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Offroad" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Offroad" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Offroad" "AllowTunes"
                    ENDIF 

                    BREAK
                CASE 7
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Bikes" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Bikes" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Bikes" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 8
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Racecars" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Racecars" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Racecars" "AllowTunes"
                    ENDIF 
                
                    BREAK
                CASE 9
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Mixed" "AllowTunes" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Mixed" "AllowTunes"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Mixed" "AllowTunes"
                    ENDIF 
                
                    BREAK
                DEFAULT
                    NOP
                    BREAK
            ENDSWITCH
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_KEY_P
            SWITCH offs
                CASE 0
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Exotics" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Exotics" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Exotics" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 1
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Tuners" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Tuners" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Tuners" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 2
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Muscles" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Muscles" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Muscles" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 3
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Lowriders" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 4
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Coupes" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Coupes" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Coupes" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 5
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Sedans" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Sedans" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Sedans" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 6
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Offroads" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Offroads" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Offroads" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 7
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Bikes" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Bikes" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Bikes" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 8
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Racecars" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Racecars" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Racecars" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                CASE 9
                    READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Mixed" "AllowPaintjobs" selected
                    IF selected=1
                        WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" "Mixed" "AllowPaintjobs"
                    ELSE
                        WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" "Mixed" "AllowPaintjobs"
                    ENDIF 
                
                    BREAK
                DEFAULT
                    NOP
                    BREAK
            ENDSWITCH
        ENDIF

        IF IS_SELECT_MENU_JUST_PRESSED
        OR IS_KEY_JUST_PRESSED VK_SPACE
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 0
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 1 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 1
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 2 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 2
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 3 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 3
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 4 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 4
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 5 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 5
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 6 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 6
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 7 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 7
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 8 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 8
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 9 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 9
                    DELETE_MENU menu
                    GET_LABEL_POINTER Dumper2 offs
                    WRITE_MEMORY offs 4 10 0
                    IF NOT isnew=11
                        isnew=2
                    ENDIF
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 10
                    DELETE_MENU menu
                    GOSUB createlabels
                    isTrue=1
                    GOTO customracemenu
                    BREAK
                CASE 11
                    DELETE_MENU menu
                    IF isnew=11
                        isnew=1
                    ENDIF
                    GOTO racermenu
                    BREAK
            ENDSWITCH
        ENDIF

        IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
            DELETE_MENU menu
            IF isnew=11
                isnew=1
            ENDIF
            GOTO racermenu
        ENDIF


    ENDWHILE

//
setactivecolumns:
    GET_TEXT_LABEL_STRING RMFFME1 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 0 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME2 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 1 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME3 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
       // ACTIVATE_MENU_ITEM menu 2 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME4 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 3 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME5 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 4 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME6 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 5 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME7 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 6 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME8 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 7 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME9 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 8 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME0 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        //ACTIVATE_MENU_ITEM menu 9 0
    ENDIF
    RETURN
//
setactivecolumns2:
    GET_TEXT_LABEL_STRING RMFFME1 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 0 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME2 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 1 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME3 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
       ACTIVATE_MENU_ITEM menu 2 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME4 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 3 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME5 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 4 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME6 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 5 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME7 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 6 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME8 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 7 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME9 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 8 0
    ENDIF
    GET_TEXT_LABEL_STRING RMFFME0 idd
    IF IS_STRING_EQUAL $idd "-" 2 0 "z"
        ACTIVATE_MENU_ITEM menu 9 0
    ENDIF
    RETURN
//
//
customracemenu:
    GOSUB findcustomcats
    IF READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "CUSTOM_CAT1" "1" offs
        WAIT 0
    ELSE
        PRINT_FORMATTED_NOW "No custom categories found" 1000
        GOTO caterace
    ENDIF
    CREATE_MENU RCCUSST (30.0 100.0) (180.0) 1 TRUE TRUE 0 (menu)
    SET_MENU_COLUMN menu 0 DUMMY (RMFFME1 RMFFME2 RMFFME3 RMFFME4 RMFFME5 RMFFME6 RMFFME7 RMFFME8 RMFFME9 RMFFME0 RCFFNEX RCFEXIT)
    GOSUB setactivecolumns2
    WHILE TRUE
        WAIT 0

        DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 369.5 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~WASD / Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 384.7 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~O and P~w~: Change tunes/paintjobs" DRAW_EVENT_BEFORE_HUD 555.0 400.0 0.12 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 415.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
        DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 430.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200
        
        GET_MENU_ITEM_SELECTED menu selected

        IF selected<10
            GET_LABEL_POINTER Customcate coords
            offs=4*selected
            coords+=offs
            READ_MEMORY coords 4 0 offs

            STRING_FORMAT idd "CUSTOM_CAT%i" offs
            READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" $idd "AllowTunes" offs
            IF offs=1
                DRAW_STRING_EXT "Spawn with tunes: ~g~ON" DRAW_EVENT_BEFORE_HUD 535.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
            ELSE
                DRAW_STRING_EXT "Spawn with tunes: ~g~OFF" DRAW_EVENT_BEFORE_HUD 535.0 305.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
            ENDIF
            READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" $idd "AllowPaintjobs" offs
            IF offs=1
                DRAW_STRING_EXT "Spawn with Paintjobs: ~g~ON" DRAW_EVENT_BEFORE_HUD 535.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
            ELSE
                DRAW_STRING_EXT "Spawn with Paintjobs: ~g~OFF" DRAW_EVENT_BEFORE_HUD 535.0 325.0 0.3 0.7 0 1 1 0 160.0 1 255 255 150 255 1 1 0 0 0 255 1 150 150 150 150
            ENDIF
            
            IF IS_KEY_JUST_PRESSED VK_KEY_O
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" $idd "AllowTunes" offs
                IF offs=1
                    WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" $idd "AllowTunes"
                ELSE
                    WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" $idd "AllowTunes"
                ENDIF 
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_KEY_P
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" $idd "AllowPaintjobs" offs
                IF offs=1
                    WRITE_INT_TO_INI_FILE 0 "CLEO/Race creator++/General settings.ini" $idd "AllowPaintjobs"
                ELSE
                    WRITE_INT_TO_INI_FILE 1 "CLEO/Race creator++/General settings.ini" $idd "AllowPaintjobs"
                ENDIF 
            ENDIF
        ENDIF

        IF IS_SELECT_MENU_JUST_PRESSED
        OR IS_KEY_JUST_PRESSED VK_SPACE
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 0
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF

                    GET_LABEL_POINTER Customcate offs
                    READ_MEMORY offs 4 0 isTrue
                    offs+=40
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 1
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=4
                    READ_MEMORY offs 4 0 isTrue
                    offs+=36
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 2
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=8
                    READ_MEMORY offs 4 0 isTrue
                    offs+=32
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 3
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=12
                    READ_MEMORY offs 4 0 isTrue
                    offs+=28
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 4
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=16
                    READ_MEMORY offs 4 0 isTrue
                    offs+=24
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 5
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=20
                    READ_MEMORY offs 4 0 isTrue
                    offs+=20
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 6
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=24
                    READ_MEMORY offs 4 0 isTrue
                    offs+=16
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 7
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=28
                    READ_MEMORY offs 4 0 isTrue
                    offs+=12
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 8
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=32
                    READ_MEMORY offs 4 0 isTrue
                    offs+=8
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 9
                    IF NOT isnew=11
                        isnew=4
                    ELSE
                        isnew=12
                    ENDIF
                    GET_LABEL_POINTER Customcate offs
                    offs+=36
                    READ_MEMORY offs 4 0 isTrue
                    offs+=4
                    WRITE_MEMORY offs 4 isTrue 0
                    DELETE_MENU menu
                    GOSUB removelabels
                    CREATE_LIST DATATYPE_INT list
                            goto testa
                            DELETE_LIST list
                    GOTO mainmenu
                    BREAK
                CASE 10
                    IF almacen=10
                        DELETE_MENU menu
                        GOSUB removelabels
                        GOSUB createlabels
                        GOSUB findcustomcats
                        CREATE_MENU RCCUSST (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
                        SET_MENU_COLUMN menu 0 DUMMY (RMFFME1 RMFFME2 RMFFME3 RMFFME4 RMFFME5 RMFFME6 RMFFME7 RMFFME8 RMFFME9 RMFFME0 RCFFNEX RCFEXIT)         
                        GOSUB setactivecolumns                    
                    ENDIF
                    BREAK
                CASE 11
                    DELETE_MENU menu
                    GOSUB removelabels
                    GOTO caterace
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE

findcustomcats:
    STRING_FORMAT idd "CUSTOM_CAT%i" isTrue
    almacen=0//
        WHILE READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" $idd "1" offs
            WAIT 0
            almacen+=1
            GET_LABEL_POINTER Coords filen
            READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" $idd "Name" filen
            SWITCH almacen
                CASE 1
                    ADD_TEXT_LABEL RMFFME1 $filen
                    GET_LABEL_POINTER Customcate coords
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 2
                    ADD_TEXT_LABEL RMFFME2 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=4
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 3
                    ADD_TEXT_LABEL RMFFME3 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=8
                    WRITE_MEMORY coords 4 isTrue 0                
                    BREAK
                CASE 4
                    ADD_TEXT_LABEL RMFFME4 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=12
                    WRITE_MEMORY coords 4 isTrue 0                
                    BREAK
                CASE 5
                    ADD_TEXT_LABEL RMFFME5 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=16
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 6
                    ADD_TEXT_LABEL RMFFME6 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=20
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 7
                    ADD_TEXT_LABEL RMFFME7 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=24
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 8
                    ADD_TEXT_LABEL RMFFME8 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=28
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 9
                    ADD_TEXT_LABEL RMFFME9 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=32
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
                CASE 10
                    ADD_TEXT_LABEL RMFFME0 $filen
                    GET_LABEL_POINTER Customcate coords
                    coords+=36
                    WRITE_MEMORY coords 4 isTrue 0
                    BREAK
            ENDSWITCH
            isTrue+=1
            STRING_FORMAT idd "CUSTOM_CAT%i" isTrue
            IF almacen=10
                RETURN
            ENDIF
        ENDWHILE
RETURN
//
infocuscar:
    SET_TEXT_FONT 2
    GET_LABEL_POINTER Coords coords
    STRING_FORMAT coords "Slot_%i" almacen
    ADD_TEXT_LABEL RMFFME0 $coords
    GET_LABEL_POINTER Istring offs
    STRING_FORMAT offs "%i" almacen
    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $offs selected
        GET_LABEL_POINTER Istring filen
        GET_NAME_OF_VEHICLE_MODEL selected filen
        STRING_FORMAT coords "Car: ~b~%s" filen
        ADD_TEXT_LABEL RMFFME1 $coords
        STRING_FORMAT coords "RandomTunes%i" almacen
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
        AND selected=1
            STRING_FORMAT coords "Tunes: ~y~Random"
            ADD_TEXT_LABEL RMFFME2 $coords
        ELSE
            STRING_FORMAT coords "Tunes: ~y~Custom"
            ADD_TEXT_LABEL RMFFME2 $coords
        ENDIF
        STRING_FORMAT coords "Paintjob%i" almacen
        IF READ_STRING_FROM_INI_FILE $filename "Vehicles ids" $coords idd
        AND IS_STRING_EQUAL $idd "RANDOM" 7 0 "z"
            STRING_FORMAT coords "Paintjob: ~y~Random"
            ADD_TEXT_LABEL RMFFME3 $coords
        ELSE
            IF IS_STRING_EQUAL $idd "-1" 2 0 "z"
                STRING_FORMAT coords "Paintjob: ~y~none"
                ADD_TEXT_LABEL RMFFME3 $coords
            ELSE
                STRING_FORMAT coords "Paintjob%i" almacen
                IF READ_STRING_FROM_INI_FILE $filename "Vehicles ids" $coords idd
                    STRING_FORMAT coords "Paintjob: ~y~%s" $idd
                    ADD_TEXT_LABEL RMFFME3 $coords
                ELSE
                    STRING_FORMAT coords "Paintjob: ~y~none"
                    ADD_TEXT_LABEL RMFFME3 $coords
                ENDIF
            ENDIF
        ENDIF
    ELSE
        ADD_TEXT_LABEL RMFFME1 "Car: ~b~none"
        ADD_TEXT_LABEL RMFFME2 "Tunes: ~y~none"
        ADD_TEXT_LABEL RMFFME3 "Paintjob: ~y~none"
    ENDIF
    DISPLAY_TEXT 550.0 150.0 RMFFME0
    DISPLAY_TEXT 550.0 175.0 RMFFME1
    DISPLAY_TEXT 550.0 200.0 RMFFME2
    DISPLAY_TEXT 550.0 225.0 RMFFME3
    RETURN
//
textcustom:
    SET_TEXT_SCALE 0.5 1.4
    SET_TEXT_COLOUR 185 255 255 185
    /*SET_TEXT_FONT 1
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_JUSTIFY 0
    SET_TEXT_DROPSHADOW 0 0 0 0 0
    SET_TEXT_EDGE 1 0 0 0 255
    SET_TEXT_COLOUR 240 255 255 255*/
RETURN
//
customopp:
    //CREATE_MENU RCFOPTI (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
    //SET_MENU_COLUMN menu 0 DUMMY (RCFLLPP RCFXWSE RCFXOPA RCFXOJO RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
    almacen=1
    SET_TEXT_DRAW_BEFORE_FADE 1
    USE_TEXT_COMMANDS 1
    GOSUB infocuscar
    ADD_TEXT_LABEL RMFFME4 "-"
    ADD_TEXT_LABEL RMFFME5 "+"
    GET_LABEL_POINTER MenusHelp selected
    WRITE_MEMORY selected 4 0 0
    IF READ_STRING_FROM_INI_FILE $filename "Vehicles ids" "Paintjob1" idd
    AND IS_STRING_EQUAL $idd "RANDOM" 7 0 "z"
        GET_LABEL_POINTER MenusHelp coords
        coords+=4
        WRITE_MEMORY coords 4 1 0
    ELSE
        GET_LABEL_POINTER MenusHelp coords
        coords+=4
        WRITE_MEMORY coords 4 0 0
    ENDIF
    IF READ_INT_FROM_INI_FILE $filename "Vehicle ids" "RandomTunes1" offs
    AND offs=1
        GET_LABEL_POINTER MenusHelp coords
        coords+=8
        WRITE_MEMORY coords 4 1 0
    ELSE
        GET_LABEL_POINTER MenusHelp coords
        coords+=8
        WRITE_MEMORY coords 4 0 0
    ENDIF
    WHILE TRUE
        WAIT 0
        GOSUB infocuscar
        //slotsinfo interface
        DRAW_RECT 580.0 200.0 100.0 120.0 0 0 20 185//
        DRAW_RECT 580.0 142.0 100.0 5.0 0 50 100 255
        DRAW_RECT 530.0 200.0 5.0 120.0 0 50 100 255
        DRAW_RECT 632.0 200.0 5.0 120.0 0 50 100 255
        DRAW_RECT 580.0 257.5 100.0 5.0 0 50 100 255

        //opponents interface

        //Title
        SET_TEXT_FONT 0
        SET_TEXT_COLOUR 185 255 255 190
        SET_TEXT_SCALE 0.6 1.6
        DISPLAY_TEXT 30.0 70.0 RCFRCSE
        SET_TEXT_SCALE 0.2 1.1
        DISPLAY_TEXT 30.0 90.0 RC_MN-N
        SET_TEXT_SCALE 0.2 1.1
        DISPLAY_TEXT 30.0 100.0 RC_MN-B

        //Boxes
        DRAW_RECT 100.5 153.0 180.0 180.0 35 10 0 195
        DRAW_RECT 88.5 123.0 80.0 20.0 0 0 0 195//slots
        DRAW_RECT 33.5 123.0 20.0 20.0 0 0 0 195//slots -
        DRAW_RECT 143.6 123.0 20.0 20.0 0 0 0 195//slots +
        DRAW_RECT 88.5 153.0 130.0 20.0 0 0 0 195// change car
        DRAW_RECT 88.5 183.0 130.0 20.0 0 0 0 195// r tunes
        DRAW_RECT 88.5 213.0 130.0 20.0 0 0 0 195// rpaint
        DRAW_RECT 165.5 183.0 20.0 20.0 0 0 0 195
        DRAW_RECT 165.5 213.0 20.0 20.0 0 0 0 195
        DRAW_RECT 100.5 65.0 180.0 5.0 150 150 150 255
        DRAW_RECT 100.5 241.0 180.0 5.0 150 150 150 255


        //Get random t/p states
        GET_LABEL_POINTER MenusHelp coords
        coords+=4
        READ_MEMORY coords 4 0 offs
        IF offs=1
            DRAW_RECT 165.5 183.0 10.0 10.0 255 255 100 195
        ENDIF
        coords+=4
        READ_MEMORY coords 4 0 offs
        IF offs=1
            DRAW_RECT 165.5 213.0 10.0 10.0 255 255 100 195
        ENDIF

        //slot+-
        GOSUB textcustom
        DISPLAY_TEXT 77.0 117.0 RCFLLPP
        GET_LABEL_POINTER MenusHelp coords
        READ_MEMORY coords 4 0 selected

        //changecar
        IF selected=2
            SET_TEXT_SCALE 0.5 1.5
            //SET_TEXT_COLOUR 200 255 255 245
            SET_TEXT_COLOUR 255 255 150 240
        ELSE
            GOSUB textcustom
        ENDIF
        DISPLAY_TEXT 30.0 147.0 RCFXWSE

        //randompaintj
        GET_LABEL_POINTER MenusHelp coords
        READ_MEMORY coords 4 0 selected
        IF selected=3
            SET_TEXT_SCALE 0.5 1.5
            SET_TEXT_COLOUR 255 255 150 240
        ELSE
            GOSUB textcustom
        ENDIF
        SET_TEXT_SCALE 0.4 1.3
        DISPLAY_TEXT 30.0 177.0 RCFXOJO

        //randomtunes
        GET_LABEL_POINTER MenusHelp coords
        READ_MEMORY coords 4 0 selected
        IF selected=4
            SET_TEXT_SCALE 0.5 1.5
            SET_TEXT_COLOUR 255 255 150 240
        ELSE
            GOSUB textcustom
        ENDIF
        DISPLAY_TEXT 30.0 207.0 RCFXOPA

        //-
        GET_LABEL_POINTER MenusHelp coords
        READ_MEMORY coords 4 0 selected
        IF selected=0
            SET_TEXT_SCALE 0.5 1.5
            SET_TEXT_COLOUR 255 255 150 240
        ELSE
            GOSUB textcustom
        ENDIF
        DISPLAY_TEXT 29.5 116.0 RMFFME4

        //+
        GET_LABEL_POINTER MenusHelp coords
        READ_MEMORY coords 4 0 selected
        IF selected=1
            SET_TEXT_SCALE 0.5 1.5
            SET_TEXT_COLOUR 255 255 150 240
        ELSE
            GOSUB textcustom
        ENDIF
        DISPLAY_TEXT 139.0 117.0 RMFFME5

        //
        GET_LABEL_POINTER MenusHelp coords
        READ_MEMORY coords 4 0 selected
        IF IS_KEY_JUST_PRESSED VK_DOWN
        OR IS_KEY_JUST_PRESSED VK_RIGHT
            IF selected=4
                selected=0
            ELSE
                selected+=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_UP
        OR IS_KEY_JUST_PRESSED VK_LEFT
            IF selected=0
                selected=4
            ELSE
                selected-=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_SPACE
            GET_LABEL_POINTER MenusHelp coords
            READ_MEMORY coords 4 0 selected
            GET_AUDIO_SFX_VOLUME angle
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 1
            SWITCH selected
                CASE 0
                    IF NOT almacen<2
                        almacen-=1
                        GOSUB infocuscar
                        GET_LABEL_POINTER Coords coords
                        STRING_FORMAT coords "Paintjob%i" almacen
                        IF READ_STRING_FROM_INI_FILE $filename "Vehicles ids" $coords idd
                        AND IS_STRING_EQUAL $idd "RANDOM" 7 0 "z"
                            GET_LABEL_POINTER MenusHelp coords
                            coords+=4
                            WRITE_MEMORY coords 4 1 0
                        ELSE
                            GET_LABEL_POINTER MenusHelp coords
                            coords+=4
                            WRITE_MEMORY coords 4 0 0
                        ENDIF
                        GET_LABEL_POINTER Coords coords
                        STRING_FORMAT coords "RandomTunes%i" almacen
                        IF READ_INT_FROM_INI_FILE $filename "Vehicle ids" $coords offs
                        AND offs=1
                            GET_LABEL_POINTER MenusHelp coords
                            coords+=8
                            WRITE_MEMORY coords 4 1 0
                        ELSE
                            GET_LABEL_POINTER MenusHelp coords
                            coords+=8
                            WRITE_MEMORY coords 4 0 0
                        ENDIF
                    ENDIF
                    BREAK
                CASE 1
                    almacen+=1
                    GET_LABEL_POINTER Istring offs
                    STRING_FORMAT offs "%i" almacen
                    GET_LABEL_POINTER Coords coords
                    IF NOT READ_STRING_FROM_INI_FILE $filename "Vehicle Positions" $offs coords
                        almacen-=1
                    ENDIF
                    GOSUB infocuscar
                    BREAK        
                CASE 2
                    DELETE_MENU menu
                    USE_TEXT_COMMANDS 0
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    GOSUB changeoppcar
                    SET_TEXT_DRAW_BEFORE_FADE 1
                    USE_TEXT_COMMANDS 1
                    //CREATE_MENU RCFOPTI (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
                    //SET_MENU_COLUMN menu 0 DUMMY (RCFLLPP RCFXWSE RCFXOPA RCFXOJO RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
                    BREAK
                CASE 4
                    //PRINT_FORMATTED_NOW "~y~Random tunes~w~ activated for this opponent!" 1000
                    GET_LABEL_POINTER MenusHelp coords
                    coords+=8
                    READ_MEMORY coords 4 0 selected
                    IF selected=0
                        GET_LABEL_POINTER Istring offs
                        STRING_FORMAT offs "RandomTunes%i" almacen
                        WRITE_INT_TO_INI_FILE 1 $filename "Vehicles ids" $offs
                        WRITE_MEMORY coords 4 1 0
                    ELSE
                        GET_LABEL_POINTER Istring offs
                        STRING_FORMAT offs "RandomTunes%i" almacen
                        WRITE_INT_TO_INI_FILE 0 $filename "Vehicles ids" $offs
                        WRITE_MEMORY coords 4 0 0
                    ENDIF
                    GOSUB infocuscar
                    BREAK
                CASE 3
                    //PRINT_FORMATTED_NOW "~y~Random paintjobs~w~ activated for this opponent!" 1000
                    GET_LABEL_POINTER Istring offs
                    GET_LABEL_POINTER MenusHelp coords
                    coords+=4
                    READ_MEMORY coords 4 0 selected
                    IF selected=0
                        STRING_FORMAT offs "Paintjob%i" almacen
                        idd="RANDOM"
                        WRITE_STRING_TO_INI_FILE $idd $filename "Vehicles ids" $offs
                        WRITE_MEMORY coords 4 1 0
                    ELSE
                        STRING_FORMAT offs "Paintjob%i" almacen
                        WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $offs
                        WRITE_MEMORY coords 4 -1 0
                    ENDIF
                    GOSUB infocuscar
                    BREAK
                ENDSWITCH
        ENDIF
        IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
            DELETE_MENU menu
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    USE_TEXT_COMMANDS 0
                    REMOVE_TEXT_LABEL RMFFME0
                    REMOVE_TEXT_LABEL RMFFME1
                    REMOVE_TEXT_LABEL RMFFME2
                    REMOVE_TEXT_LABEL RMFFME3
                    GOTO racermenu
        ENDIF
        /*GET_MENU_ITEM_SELECTED menu selected
        IF selected=0
            IF NOT IS_HELP_MESSAGE_BEING_DISPLAYED
                PRINT_HELP_FORMATTED "Press left and right arrows to change opponent slot"
            ENDIF
            PRINT_FORMATTED_NOW "Opponent slot: ~y~<%i>" 100 almacen
            IF IS_KEY_JUST_PRESSED VK_LEFT
                IF NOT almacen<2
                    almacen-=1
                    GOSUB infocuscar
                ENDIF
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_RIGHT
                almacen+=1
                GET_LABEL_POINTER Istring offs
                STRING_FORMAT offs "%i" almacen
                GET_LABEL_POINTER Coords coords
                IF NOT READ_STRING_FROM_INI_FILE $filename "Vehicle Positions" $offs coords
                almacen-=1
                ENDIF
                    GOSUB infocuscar
                ENDIF
        ELSE
            PRINT_FORMATTED_NOW "Opponent slot: %i" 100 almacen
        ENDIF 
        IF IS_SELECT_MENU_JUST_PRESSED
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 1
                    DELETE_MENU menu
                    USE_TEXT_COMMANDS 0
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    GOSUB changeoppcar
                    SET_TEXT_DRAW_BEFORE_FADE 1
                    USE_TEXT_COMMANDS 1
                    CREATE_MENU RCFOPTI (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY (RCFLLPP RCFXWSE RCFXOPA RCFXOJO RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
                    BREAK
                CASE 2
                    PRINT_FORMATTED_NOW "~y~Random tunes~w~ activated for this opponent!" 1000
                    GET_LABEL_POINTER Istring offs
                    STRING_FORMAT offs "RandomTunes%i" almacen
                    WRITE_INT_TO_INI_FILE 1 $filename "Vehicles ids" $offs
                    WAIT 500
                    GOSUB infocuscar
                    BREAK
                CASE 3
                    PRINT_FORMATTED_NOW "~y~Random paintjobs~w~ activated for this opponent!" 1000
                    GET_LABEL_POINTER Istring offs
                    STRING_FORMAT offs "Paintjob%i" almacen
                    idd="RANDOM"
                    WRITE_STRING_TO_INI_FILE $idd $filename "Vehicles ids" $offs
                    WAIT 500
                    GOSUB infocuscar
                    BREAK
                CASE 4
                    DELETE_MENU menu
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    USE_TEXT_COMMANDS 0
                    REMOVE_TEXT_LABEL RMFFME0
                    REMOVE_TEXT_LABEL RMFFME1
                    REMOVE_TEXT_LABEL RMFFME2
                    REMOVE_TEXT_LABEL RMFFME3
                    GOTO racermenu
                    BREAK
            ENDSWITCH     
        ENDIF*/
    ENDWHILE

//
changeoppcar:
    SET_PLAYER_CONTROL 0 0
    CREATE_MENU RCFOPTI (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
    SET_MENU_COLUMN menu 0 DUMMY (RCFXPLY RCFXIDV RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
    WHILE TRUE
        WAIT 0
        IF IS_SELECT_MENU_JUST_PRESSED
            GET_MENU_ITEM_SELECTED menu selected
            SWITCH selected
                CASE 0
                    DELETE_MENU menu
                    PRINT_HELP_FOREVER RCFXHEH
                    SET_PLAYER_CONTROL 0 1
                    idd="Enabled"
                    GET_LABEL_POINTER Opponentslap coords
                    GET_LABEL_POINTER Coords2 filen
                    STRING_FORMAT coords "Enabled"
                    STRING_FORMAT filen "Enabled"
                    WHILE TRUE
                        WAIT 0  
                        PRINT_FORMATTED_NOW "Save colours: ~y~%s~w~ / Save tunes: ~y~%s~w~ / Save paintjob: ~y~%s" 50 $filen $idd $coords
                        
                        IF IS_KEY_JUST_PRESSED VK_KEY_T
                            IF IS_STRING_EQUAL $idd "Enabled" 10 0 "z"
                                idd="Disabled"
                            ELSE
                                idd="Enabled"
                            ENDIF
                        ENDIF  
                        IF IS_KEY_JUST_PRESSED VK_KEY_P
                            IF IS_STRING_EQUAL $coords "Enabled" 10 0 "z"
                                STRING_FORMAT coords "Disabled"
                            ELSE
                                STRING_FORMAT coords "Enabled"
                            ENDIF  
                        ENDIF

                        IF IS_KEY_JUST_PRESSED VK_KEY_U
                            IF IS_STRING_EQUAL $filen "Enabled" 10 0 "z"
                                STRING_FORMAT filen "Disabled"
                            ELSE
                                STRING_FORMAT filen "Enabled"
                            ENDIF  
                        ENDIF
                        IF IS_KEY_JUST_PRESSED VK_KEY_K
                            IF IS_CHAR_IN_ANY_CAR scplayer
                                GET_CAR_CHAR_IS_USING scplayer car
                                GOSUB savecar
                                IF IS_STRING_EQUAL $filen "Enabled" 10 0 "z"
                                    GOSUB savecolors
                                ELSE
                                    GET_LABEL_POINTER coords selected
                                    STRING_FORMAT selected "Colour1Car%i" almacen
                                    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $selected offs
                                        WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $selected
                                    ENDIF
                                    STRING_FORMAT selected "Colour2Car%i" almacen
                                    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $selected offs
                                        WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $selected
                                    ENDIF
                                    STRING_FORMAT selected "Colour3Car%i" almacen
                                    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $selected offs
                                        WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $selected
                                    ENDIF
                                    STRING_FORMAT selected "Colour4Car%i" almacen
                                    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $selected offs
                                        WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $selected
                                    ENDIF
                                ENDIF
                                IF IS_STRING_EQUAL $coords "Enabled" 10 0 "z"
                                    GOSUB savePaintjob
                                ELSE
                                    GET_LABEL_POINTER Coords selected
                                    STRING_FORMAT selected "Paintjob%i" almacen
                                    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $selected offs 
                                        WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $selected 
                                    ENDIF
                                ENDIF
                                IF IS_STRING_EQUAL $idd "ENABLED" 10 0 "z"
                                    GOSUB saveCarGetMods
                                ELSE
                                    GOSUB deleteCarMods
                                ENDIF
                                GET_LABEL_POINTER Coords coords
                                STRING_FORMAT coords "RandomTunes%i" almacen
                                IF READ_STRING_FROM_INI_FILE $filename "Vehicles ids" $coords idd
                                    WRITE_INT_TO_INI_FILE 0 $filename "Vehicles ids" $coords 
                                ENDIF
                                CLEAR_HELP
                                GOTO changeoppcar
                            ELSE
                                PRINT_FORMATTED_NOW "~r~You need to be in a car!" 1000
                                WAIT 500
                                GET_AUDIO_SFX_VOLUME angle
                                CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                            ENDIF
                        ENDIF
                    ENDWHILE
                    BREAK
                //
                CASE 1
                    DELETE_MENU menu
                    PRINT_HELP_FOREVER RCENEAS
                    CLEO_CALL GetFormString 0 (selected)
                    CLEAR_HELP
                    IF NOT selected=-1
                        GET_LABEL_POINTER Istring offs
                        STRING_FORMAT offs "%i" almacen
                        WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $offs 
                        GOSUB deleteCarMods
                        GET_LABEL_POINTER Coords selected
                        STRING_FORMAT selected "Paintjob%i" almacen
                        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $selected offs 
                            WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $selected 
                        ENDIF
                    ENDIF
                    CREATE_MENU RCFOPTI (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
                    SET_MENU_COLUMN menu 0 DUMMY (RCFXPLY RCFXIDV RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
                    BREAK
                CASE 2
                    DELETE_MENU menu
                    RETURN
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE
//
deleteCarMods:
    offs=0
    WHILE NOT offs=16
        WAIT 0
        GET_LABEL_POINTER Coords coords
        SWITCH offs
            CASE 0
                STRING_FORMAT coords "Hood%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK
            CASE 1
                STRING_FORMAT coords "Vent%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK            
            CASE 2
                STRING_FORMAT coords "Spoiler%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 3
                STRING_FORMAT coords "SideSkirt%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 4
                STRING_FORMAT coords "FBullbar%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 5
                STRING_FORMAT coords "RBullbar%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 6
                STRING_FORMAT coords "Lights%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 7
                STRING_FORMAT coords "Roof%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 8
                STRING_FORMAT coords "Nitro%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            //CASE 9
                //STRING_FORMAT coords "Hydraulics%i" almacen
                //IF NOT selected=-1
                    //WRITE_INT_TO_INI_FILE $filename "Vehicles ids" $coords selected
                //ENDIF
                //BREAK   
            /*CASE 10
                STRING_FORMAT coords "Stereo%i" almacen
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE $filename "Vehicles ids" $coords selected
                ENDIF
                BREAK*/   
            CASE 12
                STRING_FORMAT coords "Wheels%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK 
            CASE 13
                STRING_FORMAT coords "Exhaust%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK
            CASE 14
                STRING_FORMAT coords "FBumper%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 15
                STRING_FORMAT coords "RBumper%i" almacen
                IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
                    WRITE_INT_TO_INI_FILE -1 $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
        ENDSWITCH
        offs+=1
    ENDWHILE
    RETURN

//
savecar:
    GET_LABEL_POINTER Coords selected
    GET_CAR_MODEL car offs
    STRING_FORMAT selected "%i" almacen
    WRITE_INT_TO_INI_FILE offs $filename "Vehicles ids" $selected
RETURN
//
savecolors:
    GET_CAR_CHAR_IS_USING scplayer car
    GET_CAR_COLOURS car offs selected
    GET_LABEL_POINTER Coords filen
    STRING_FORMAT filen "Colour1Car%i" almacen
    WRITE_INT_TO_INI_FILE offs $filename "Vehicles ids" $filen
    STRING_FORMAT filen "Colour2Car%i" almacen
    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $filen 
    GET_EXTRA_CAR_COLOURS car offs selected
    STRING_FORMAT filen "Colour3Car%i" almacen
    WRITE_INT_TO_INI_FILE offs $filename "Vehicles ids" $filen 
    STRING_FORMAT filen "Colour4Car%i" almacen
    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $filen 
    RETURN
//
savePaintjob:
    GET_CAR_CHAR_IS_USING scplayer car
    GET_LABEL_POINTER Coords selected
    STRING_FORMAT selected "Paintjob%i" almacen
    GET_CURRENT_VEHICLE_PAINTJOB car offs
    WRITE_INT_TO_INI_FILE offs $filename "Vehicles ids" $selected
    RETURN
//
saveCarGetMods:
    GET_CAR_CHAR_IS_USING scplayer car
    GET_LABEL_POINTER Coords coords
    offs=0
    WHILE NOT offs=16
        WAIT 0
        GET_AVAILABLE_VEHICLE_MOD car offs selected
        GET_VEHICLE_MOD_TYPE selected coords
        IF coords>0
        AND coords<16
            GET_CURRENT_CAR_MOD car offs selected
        ELSE
          selected=-1
        ENDIF
        GET_LABEL_POINTER Coords coords
        SWITCH offs
            CASE 0
                STRING_FORMAT coords "Hood%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving hood" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK
            CASE 1
                STRING_FORMAT coords "Vent%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving vent" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK            
            CASE 2
                STRING_FORMAT coords "Spoiler%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving spoiler" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF 
                BREAK   
            CASE 3
                STRING_FORMAT coords "SideSkirt%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving side skirts" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                    WAIT 0
                ENDIF
                BREAK   
            CASE 4
                STRING_FORMAT coords "FBullbar%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving Front bullbar" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 5
                STRING_FORMAT coords "RBullbar%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving Rear bullbar" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 6
                STRING_FORMAT coords "Lights%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving lights" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 7
                STRING_FORMAT coords "Roof%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving roof" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            CASE 8
                STRING_FORMAT coords "Nitro%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving hood" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
            //CASE 9
                //STRING_FORMAT coords "Hydraulics%i" almacen
                //IF NOT selected=-1
                    //WRITE_INT_TO_INI_FILE $filename "Vehicles ids" $coords selected
                //ENDIF
                //BREAK   
            /*CASE 10
                STRING_FORMAT coords "Stereo%i" almacen
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE $filename "Vehicles ids" $coords selected
                ENDIF
                BREAK*/   
            CASE 12
                STRING_FORMAT coords "Wheels%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving Wheels" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK 
            CASE 13
                STRING_FORMAT coords "Exhaust%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving exhaust" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK
            CASE 14
                STRING_FORMAT coords "FBumper%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving Front bumper" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords 
                ENDIF
                BREAK   
            CASE 15
                STRING_FORMAT coords "RBumper%i" almacen
                IF selected=-2
                    PRINT_FORMATTED "Error saving Rear Bumper" 1000
                    selected=-1
                ENDIF
                IF NOT selected=-1
                    WRITE_INT_TO_INI_FILE selected $filename "Vehicles ids" $coords
                ENDIF
                BREAK   
        ENDSWITCH
        offs+=1
    ENDWHILE
    RETURN
//

specialcars:
    almacen=1
    g-=1
    menu=g
    g+=1
    SET_PLAYER_CONTROL_PAD_MOVEMENT 0 0
    GET_LABEL_POINTER Opponents coords
    coords+=4
    testcar=CAR
    READ_MEMORY coords 4 0 testcar
    GET_CAR_COORDINATES testcar x y z2
    POINT_CAMERA_AT_CAR testcar 3 1
    //ATTACH_CAMERA_TO_VEHICLE testcar 8.0 8.0 1.0 0.0 0.0 0.0 10.0 1
    FREEZE_CAR_POSITION car 1
    TASK_LEAVE_CAR_IMMEDIATELY scplayer car
    WHILE IS_CHAR_IN_CAR scplayer car
        WAIT 0
    ENDWHILE
    SET_CHAR_COORDINATES scplayer 0.0 0.0 -300.0
    DO_FADE 1000 1
    SET_TEXT_DRAW_BEFORE_FADE 1
    USE_TEXT_COMMANDS 1
    WHILE TRUE
        WAIT 0
        
        DRAW_STRING "Press ~y~U~w~ and ~y~X~w~ to change change between opponents, press ~y~J~w~ to start the race." DRAW_EVENT_AFTER_HUD 30.0 20.0 0.25 1.1 1 1
        DRAW_STRING "The current opponent car will change when the player is detected inside a car, the opponent will use exactly that car." DRAW_EVENT_AFTER_HUD 30.0 40.0 0.25 1.1 1 1
        DRAW_STRING "This option was made to work with ~y~Tuning mod~w~ cars, but should work with any custom/special car." DRAW_EVENT_AFTER_HUD 30.0 60.0 0.25 1.1 1 1
        
        IF IS_PLAYER_CONTROL_ON 0
            SET_PLAYER_CONTROL 0 0
            DO_FADE 10 0
            POINT_CAMERA_AT_CAR testcar 3 1
            DO_FADE 1000 1
        ENDIF
        IF IS_CHAR_IN_ANY_CAR scplayer
            WAIT 100
            DO_FADE 5 0
            PRINT_FORMATTED_NOW "Loading..." 2000
            WAIT 1800
            DO_FADE 5 0
            IF IS_CHAR_IN_ANY_CAR scplayer
                GET_CAR_CHAR_IS_USING scplayer offs
                SET_LOAD_COLLISION_FOR_CAR_FLAG offs 1
                GET_DRIVER_OF_CAR testcar char
                GET_CHAR_MODEL char char
                GET_CAR_COORDINATES testcar x y z
                GET_CAR_HEADING testcar angle
                DELETE_CAR testcar
                SET_CAR_COORDINATES offs x y z
                SET_CAR_HEADING offs angle
                WARP_CHAR_FROM_CAR_TO_COORD scplayer 0.0 0.0 -300.0
                testcar=offs
                DO_FADE 1 0
                POINT_CAMERA_AT_CAR testcar 3 1
                WAIT 1000
                DO_FADE 100 0
                GET_LABEL_POINTER Opponents coords
                selected=almacen*4
                coords+= selected
                WRITE_MEMORY coords 4 testcar 0
                wait 500
                POINT_CAMERA_AT_CAR testcar 3 1
                DO_FADE 500 1
                CREATE_CHAR_INSIDE_CAR testcar PEDTYPE_MISSION1 char char
                SET_CHAR_ONLY_DAMAGED_BY_PLAYER char 1
                SET_CHAR_PROOFS char 1 1 1 1 1
                SET_CHAR_DROWNS_IN_WATER char 0
                SET_CHAR_GET_OUT_UPSIDE_DOWN_CAR char 0
                SET_CHAR_CANT_BE_DRAGGED_OUT char 1
                SET_CHAR_CAN_BE_KNOCKED_OFF_BIKE char 1
                SET_CAR_CRUISE_SPEED testcar 100.0
                SET_CAR_CAN_GO_AGAINST_TRAFFIC testcar 1
                SET_CAR_AS_MISSION_CAR testcar
                SET_CAR_MISSION testcar 33
                SET_CAR_MISSION testcar 0
            ENDIF
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_KEY_J
            USE_TEXT_COMMANDS 0
            SET_TEXT_DRAW_BEFORE_FADE 0
            RETURN
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_KEY_U
            IF NOT almacen=1
                coords=0
                almacen-=1
                GET_LABEL_POINTER Opponents coords
                offs=almacen*4
                coords+=offs
                READ_MEMORY coords 4 0 testcar
                POINT_CAMERA_AT_CAR testcar 3 1
                SET_CAMERA_CONTROL 1
            ELSE
                coords=0
                almacen=menu
                GET_LABEL_POINTER Opponents coords
                offs=almacen*4
                coords+=offs
                READ_MEMORY coords 4 0 testcar
                POINT_CAMERA_AT_CAR testcar 3 1
            ENDIF
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_KEY_X
            coords=0
            IF NOT almacen=menu
                coords=0
                almacen+=1
                GET_LABEL_POINTER Opponents coords
                offs=almacen*4
                coords+=offs
                READ_MEMORY coords 4 0 testcar
                POINT_CAMERA_AT_CAR testcar 3 1
            ELSE
                coords=0
                almacen=1
                GET_LABEL_POINTER Opponents coords
                offs=almacen*4
                coords+=offs
                READ_MEMORY coords 4 0 testcar
                POINT_CAMERA_AT_CAR testcar 3 1
            ENDIF
        ENDIF
    ENDWHILE

RETURN
//
loadOpts:
    //opciones
        GET_LABEL_POINTER RaceOpts coords
        READ_MEMORY coords 4 0 offs//clima
        SWITCH offs
            DEFAULT
                WAIT 0
                BREAK
            CASE 1
                GET_CITY_PLAYER_IS_IN 0 offs //sunny
                SWITCH offs //ls sf lv
                    CASE 1
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "SunnyLS"  offs
                        FORCE_WEATHER_NOW offs
                        BREAK
                    CASE 2
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "SunnySF"  offs
                        FORCE_WEATHER_NOW offs
                        BREAK
                    CASE 3
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "SunnyLV"  offs
                        FORCE_WEATHER_NOW offs
                        BREAK
                ENDSWITCH
                BREAK
            CASE 2
                GET_CITY_PLAYER_IS_IN 0 offs //cloudy
                SWITCH offs //ls sf lv
                    CASE 1
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "CloudyLS"  offs
                        FORCE_WEATHER_NOW offs
                        BREAK
                    CASE 2
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "CloudySF"  offs
                        FORCE_WEATHER_NOW offs
                        BREAK
                    CASE 3
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "CloudyLV"  offs
                        FORCE_WEATHER_NOW offs
                        BREAK
                ENDSWITCH
                BREAK
            CASE 3
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Rainy" offs
                FORCE_WEATHER_NOW offs
                READ_FLOAT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "RainyIntesity" angle
                SET_RAIN_INTENSITY angle
                BREAK 
            CASE 4 //rainy and thunder are the same lol, 16 is rainy contryside
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Storm" offs
                FORCE_WEATHER_NOW offs
                READ_FLOAT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "StormIntensity" angle
                SET_RAIN_INTENSITY angle
                BREAK
            CASE 5
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Foggy" offs
                FORCE_WEATHER_NOW offs//foggy
                BREAK
            CASE 6
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Sandstorm" offs
                FORCE_WEATHER_NOW offs//Sandstorm
                BREAK    
        ENDSWITCH
        coords+=4
        READ_MEMORY coords 4 0 offs  //gravedad
        SWITCH offs
            CASE 0
                WAIT 0
                BREAK
            CASE 1
                READ_FLOAT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "VeryLow" angle
                WRITE_MEMORY 0x863984 4 angle 1
                BREAK
            CASE 2
                READ_FLOAT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Low" angle
                WRITE_MEMORY 0x863984 4 angle 1
                BREAK
            CASE 3
                READ_FLOAT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "High" angle
                WRITE_MEMORY 0x863984 4 angle 1
                BREAK
            CASE 4
                READ_FLOAT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "VeryHigh" angle
                WRITE_MEMORY 0x863984 4 angle 1
                BREAK
        ENDSWITCH

        coords+=4
        //wanted level lo cambio despues
        coords+=4
        READ_MEMORY coords 4 0 offs //trafico
        SWITCH offs
            CASE 0
                SET_CAR_DENSITY_MULTIPLIER 0.0
                BREAK
            CASE 1
                SET_CAR_DENSITY_MULTIPLIER 1.0
                BREAK
            CASE 2
                SET_CAR_DENSITY_MULTIPLIER 2.0
                BREAK
            CASE 3
                SET_CAR_DENSITY_MULTIPLIER 3.0
                BREAK
        ENDSWITCH

        coords+=4

        //damage vis lo cambio dps

        coords+=4

        READ_MEMORY coords 4 0 offs//hora
        SWITCH offs
            CASE 0
                WAIT 0
                BREAK
            CASE 1
                READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Sunrise" idd
                SCAN_STRING $idd "%i:%i" g offs selected
                SET_TIME_OF_DAY offs selected
                BREAK
            CASE 2
                READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Midday" idd
                SCAN_STRING $idd "%i:%i" g offs selected
                SET_TIME_OF_DAY offs selected
                BREAK
            CASE 3
                READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Afternoon" idd
                SCAN_STRING $idd "%i:%i" g offs selected
                SET_TIME_OF_DAY offs selected
                BREAK
            CASE 4
                READ_STRING_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "Night" idd
                SCAN_STRING $idd "%i:%i" g offs selected
                SET_TIME_OF_DAY offs selected
                BREAK
        ENDSWITCH

        coords+=4

        //wl fixed dps

        coords+=4

        READ_MEMORY coords 4 0 offs//weather fixed NO, now its mech damage (on-off)
        
        //changed later

        coords+=4

        READ_MEMORY coords 4 0 offs//timefixed
        IF offs=1
            //WRITE_MEMORY 0x969168 1 1 0
            //WRITE_MEMORY 0x53BFBD 4 0x90909090 1
            WRITE_MEMORY 0x53BFBD 5 0x90 1
        ENDIF  
        RETURN

    //
//
testa:
  //opciones
    GOSUB loadOpts
  //codigo
    idd="yes"
    CREATE_LIST DATATYPE_INT list
    
    READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
    IF offs=1
        GET_LABEL_POINTER Totlaps selected
        WRITE_MEMORY selected 4 esfera 0
        GET_LABEL_POINTER IsCircuit selected
        WRITE_MEMORY selected 4 1 0
    ELSE
        GET_LABEL_POINTER Totlaps selected
        WRITE_MEMORY selected 4 1 0
        GET_LABEL_POINTER IsCircuit selected
        WRITE_MEMORY selected 4 0 0
    ENDIF
    GET_LABEL_POINTER LapTime offs
    WRITE_MEMORY offs 4 0 0
    
    offs+=4
    WRITE_MEMORY offs 4 0 0
    GET_LABEL_POINTER Opponentschar offs
    WRITE_MEMORY offs 4 0 0
    SET_EVERYONE_IGNORE_PLAYER 0 1
    /*GET_LABEL_POINTER Coords coords
    READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
    IF SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z radio filen angle isnew
        PRINT_FORMATTED_NOW "yes" 1000
    ENDIF
    IF SCAN_STRING $coords "%f %f %f %f %i %f" offs x y z radio filen angle
        PRINT_FORMATTED_NOW "yes" 1000
    ENDIF*/
    DO_FADE 100 0
    SET_FADING_COLOUR 0 0 10
    GET_CHAR_COORDINATES scplayer x y z
    CLEAR_AREA x y z 10000.0 1
    SET_PED_DENSITY_MULTIPLIER 0.0
    SET_CREATE_RANDOM_COPS 1
    GET_LABEL_POINTER PlayerCoords coords
    STRING_FORMAT coords "%f %f %f" x y z
    GET_LABEL_POINTER PlayerInt coords
    GET_CHAR_AREA_VISIBLE scplayer offs
    WRITE_MEMORY coords 4 offs 0

    GET_LABEL_POINTER Pos coords
    WRITE_MEMORY coords 4 1 0
    
    IF NOT IS_CHAR_ON_FOOT scplayer
        GET_CAR_CHAR_IS_USING scplayer car
        GET_LABEL_POINTER IsPlayerCar offs
        WRITE_MEMORY offs 4 1 0
    ELSE
        GET_LABEL_POINTER IsPlayerCar offs
        WRITE_MEMORY offs 4 -1 0
    ENDIF
    GET_LABEL_POINTER Opponentschar offs
    offs+=4
    WRITE_MEMORY offs 4 0 0

    i=1
    g=0
    isTrue=1
    WHILE isTrue=1
        WAIT 0
        IF g=0
            IF IS_CHAR_ON_FOOT scplayer
                IF isnew=4
                OR isnew=12
                    GOSUB customcat
                ELSE
                    GOSUB randomcat
                ENDIF
                GET_CHAR_COORDINATES scplayer x y z
                CREATE_CAR selected x y z car
                WARP_CHAR_INTO_CAR scplayer car
                GET_VEHICLE_SUBCLASS car offs
                IF offs= VEHICLE_SUBCLASS_HELI
                    SET_HELI_BLADES_FULL_SPEED car
                ENDIF

                READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "AnyColourForCars" coords
                IF coords=1
                    GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
                    GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
                    CHANGE_CAR_COLOUR car offs coords
                    GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
                    GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
                    SET_EXTRA_CAR_COLOURS car offs coords
                ENDIF

                READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" $idd "AllowTunes" coords
                IF coords=1
                    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "AnyColourForCars" coords
                    IF coords=2
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
                        CHANGE_CAR_COLOUR car offs coords
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
                        SET_EXTRA_CAR_COLOURS car offs coords
                    ENDIF
                    testcar=car
                    GOSUB getmods
                    GOSUB setmods
                    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" $idd "AllowPaintjobs" coords 
                    IF coords=1
                        GOSUB setcustompaintjob
                    ENDIF
                    GET_CAR_CHAR_IS_USING scplayer car
                ENDIF 
            ENDIF
            GET_LABEL_POINTER Coords coords
            IF READ_STRING_FROM_INI_FILE $filename "Vehicle Positions" "0" coords
                SCAN_STRING $coords "%f %f %f %f %f" offs x y z radio angle 
            ELSE
                READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
                SCAN_STRING $coords "%f %f %f %f %i %f" offs x y z radio selected angle 
                angle=180.0
            ENDIF
            IF READ_INT_FROM_INI_FILE $filename "Settings" "InteriorID" offs
                //IF NOT offs=0
                    SET_AREA_VISIBLE offs
                    SET_CHAR_AREA_VISIBLE scplayer offs
                    SET_VEHICLE_AREA_VISIBLE car offs
                //ENDIF
            ENDIF
            CLEO_CALL loadObjects 0 list filename 0

            GET_LABEL_POINTER Coords2 coords
            FREE_MEMORY coords

            SET_CHAR_COORDINATES scplayer x y z
            SET_CAR_HEADING car angle
            SET_CAMERA_BEHIND_PLAYER
            GET_VEHICLE_SUBCLASS car offs
            IF offs= VEHICLE_SUBCLASS_PLANE
            OR offs= VEHICLE_SUBCLASS_FPLANE
                FREEZE_CAR_POSITION car 1
            ENDIF
            IF isnew>9
                isTrue=0
            ELSE
                g+=1
            ENDIF
        ELSE //mi auto en carrera onemake se cambia de color
            GET_LABEL_POINTER Coords coords
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" g
            IF READ_STRING_FROM_INI_FILE $filename "Vehicle Positions" $offs coords
                IF IS_STRING_EQUAL $coords "DELETED" 7 0 "p"
                    isTrue=0
                ELSE
                    SCAN_STRING $coords "%f %f %f %f %f" selected x y z radio angle
                    IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $offs selected
                    AND isnew=0
                        GET_MODEL_TYPE selected ich
                        IF ich=MODEL_TYPE_VEHICLE
                            REQUEST_MODEL selected
                            WHILE NOT HAS_MODEL_LOADED selected
                                WAIT 0
                            ENDWHILE
                        ELSE
                            GET_CAR_MODEL car selected
                            REQUEST_MODEL selected
                            WHILE NOT HAS_MODEL_LOADED selected
                                WAIT 0
                            ENDWHILE
                        ENDIF
                    ELSE
                        IF NOT isnew=2
                        AND NOT isnew=4
                            GET_CAR_MODEL car selected
                            REQUEST_MODEL selected
                            WHILE NOT HAS_MODEL_LOADED selected
                                WAIT 0
                            ENDWHILE
                            READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "TunesForOnemakeRaces" offs
                            IF offs=1
                                GOSUB getmods
                            ENDIF
                        ENDIF
                    ENDIF

                    IF isnew=2
                        GET_LABEL_POINTER Dumper2 offs
                        READ_MEMORY offs 4 0 offs
                        GOSUB randomcat
                    ENDIF
                    IF isnew=4
                        GOSUB customcat
                    ENDIF
                    
                    CREATE_CAR selected x y z testcar
                    MARK_MODEL_AS_NO_LONGER_NEEDED selected

                    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "AnyColourForCars" coords
                    IF coords=1
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
                        CHANGE_CAR_COLOUR testcar offs coords
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
                        GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
                        SET_EXTRA_CAR_COLOURS testcar offs coords
                    ENDIF

                    IF isnew=1
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "TunesForOnemakeRaces" offs
                        IF offs=1
                            car=testcar
                            GOSUB setmods
                            GET_CAR_CHAR_IS_USING scplayer car
                        ENDIF
                        READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Settings" "PaintjobsForOnemakeRaces" offs
                        IF offs=1
                            car=testcar
                            GOSUB setcustompaintjob
                            GET_CAR_CHAR_IS_USING scplayer car
                        ENDIF
                    ELSE
                        IF NOT isnew=2
                        AND NOT isnew=4
                            GOSUB setcustomcolours
                            GOSUB setcustompaintjob
                            GET_LABEL_POINTER Coords coords
                            STRING_FORMAT coords "RandomTunes%i" g
                            IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
                            AND offs=1
                                car=testcar
                                GOSUB getmods
                                GOSUB setmods
                                GET_CAR_CHAR_IS_USING scplayer car
                            ELSE
                                GOSUB setcustommods
                            ENDIF
                        ELSE
                            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" $idd "AllowTunes" coords
                            IF coords=1
                                car=testcar
                                GOSUB getmods
                                GOSUB setmods
                                
                            ENDIF
                            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" $idd "AllowPaintjobs" coords 
                            IF coords=1
                                car=testcar
                                GOSUB setcustompaintjob
                            ENDIF
                            GET_CAR_CHAR_IS_USING scplayer car
                        ENDIF                           
                    ENDIF
                    INIT_EXTENDED_CAR_VARS testcar "RaceC++" 14
                    
                    
                    SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 0
                    IF NOT radio=2.000000
                        SET_CAR_ROLL testcar radio
                    ENDIF
                    GENERATE_RANDOM_INT_IN_RANGE 11 25 ich
                    REQUEST_MODEL ich
                    WHILE NOT HAS_MODEL_LOADED ich
                        WAIT 0
                    ENDWHILE
                    CREATE_CHAR_INSIDE_CAR testcar PEDTYPE_MISSION1 ich char
                    MARK_MODEL_AS_NO_LONGER_NEEDED ich
                    SET_EXTENDED_CAR_VAR testcar "RaceC++" 9 char

                    SET_CHAR_ONLY_DAMAGED_BY_PLAYER char 1
                    SET_CHAR_PROOFS char 1 1 1 1 1
                    SET_CHAR_DROWNS_IN_WATER char 0
                    SET_CHAR_GET_OUT_UPSIDE_DOWN_CAR char 0
                    SET_CHAR_CANT_BE_DRAGGED_OUT char 1
                    SET_CHAR_CAN_BE_KNOCKED_OFF_BIKE char 1

                    GET_VEHICLE_SUBCLASS testcar ich
                    IF ich= VEHICLE_SUBCLASS_HELI
                        SET_HELI_BLADES_FULL_SPEED testcar
                    ENDIF
                    IF ich=VEHICLE_SUBCLASS_PLANE
                    OR ich=VEHICLE_SUBCLASS_FPLANE
                        PLANE_STARTS_IN_AIR testcar
                        FREEZE_CAR_POSITION testcar 1
                    ENDIF
                    GET_LABEL_POINTER IsLive coords
                    READ_MEMORY coords 4 0 offs
                    IF offs=1
                        FREEZE_CAR_POSITION testcar 1
                    ENDIF
                    IF READ_INT_FROM_INI_FILE $filename "Settings" "InteriorID" offs
                        //IF NOT offs=0
                            SET_VEHICLE_AREA_VISIBLE testcar offs
                            SET_CHAR_AREA_VISIBLE char offs
                        //ENDIF                      
                    ENDIF

                    GET_LABEL_POINTER Opponents almacen
                    offs=g*4
                    almacen+=offs
                    WRITE_MEMORY almacen 4 testcar 0
                    SET_CAR_HEADING testcar angle
                    // GET_LABEL_POINTER Opponentslap almacen
                    // almacen+=offs
                    // WRITE_MEMORY almacen 4 1 0 //poner la cantidad de vueltas a 1 (oponente g)
                    GET_LABEL_POINTER OpponentsPositions almacen
                    almacen+=offs
                    WRITE_MEMORY almacen 4 0 0
                    g+=1
                ENDIF
            ELSE
                GET_LABEL_POINTER Helper coords
                coords+=4
                WRITE_MEMORY coords 4 g 0
                isTrue=0
            ENDIF
        ENDIF
    ENDWHILE

    // REQUEST_MODEL 3228
    // WHILE not HAS_MODEL_LOADED 3228
    //     WAIT 0
    // ENDWHILE
    // CREATE_CAR 3228 x y 40.0 testcar
    //     SET_LOAD_COLLISION_FOR_CAR_FLAG testcar 0
    //     SET_CAR_FOLLOW_CAR testcar car 1000.0
    // REQUEST_MODEL 10
    // WHILE not HAS_MODEL_LOADED 10
    //     WAIT 0
    // ENDWHILE
    // CREATE_CHAR_INSIDE_CAR testcar PEDTYPE_MISSION1 10 char

    GET_LABEL_POINTER IsLive coords
    READ_MEMORY coords 4 0 offs
    GET_CAR_CHAR_IS_USING scplayer car
    IF offs=1
        MARK_CAR_AS_NEEDED car
        SET_LOAD_COLLISION_FOR_CAR_FLAG car 1
        GOSUB specialcars
        WARP_CHAR_INTO_CAR scplayer car
        FREEZE_CAR_POSITION car 1
        RESTORE_CAMERA
        SET_CAMERA_BEHIND_PLAYER
        SET_PLAYER_CONTROL_PAD_MOVEMENT 0 1
        SET_PLAYER_CONTROL 0 0
        MARK_CAR_AS_NO_LONGER_NEEDED car
        SET_LOAD_COLLISION_FOR_CAR_FLAG car 0
        
    ENDIF
    GET_LABEL_POINTER RaceOpts coords
    coords+=28
    READ_MEMORY coords 4 0 offs
    IF offs=0
        GET_CAR_CHAR_IS_USING scplayer car
        SET_CAR_PROOFS car 1 1 1 1 1
        IF IS_CHAR_IN_ANY_BOAT scplayer
            SET_CAR_HEALTH car 50000
        ENDIF
    ENDIF

    IF isnew>9
        GET_LABEL_POINTER Totlaps offs
        WRITE_MEMORY offs 4 -3 0
    ENDIF

    GET_LABEL_POINTER Opponentslap almacen
    WRITE_MEMORY almacen 4 1 0 //poner la cantidad de vueltas a 1 (jugador)
    DO_FADE 500 1
    WAIT 1000
    SET_FADING_COLOUR 0 0 0
    isTrue=1
    PRINT_BIG_FORMATTED "3" 1100 4
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_RACE_321
    WAIT 1100
    PRINT_BIG_FORMATTED "2" 1100 4
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_RACE_321
    WAIT 1100
    PRINT_BIG_FORMATTED "1" 1100 4
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_RACE_321
    WAIT 1100
    PRINT_BIG_FORMATTED "GO!" 600 4
    REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_RACE_GO

    READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
    IF offs=1
        GET_LABEL_POINTER Totlaps offs
        READ_MEMORY offs 4 0 menu
        READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "ShowLaps" offs
        IF offs=1
            IF isnew>9
                menu=0
                DISPLAY_ONSCREEN_COUNTER_WITH_STRING_LOCAL isTrue menu RCFLAPZ
            ELSE
                DISPLAY_TWO_ONSCREEN_COUNTERS_WITH_STRING_LOCAL isTrue menu RCFLAPZ
            ENDIF
        ENDIF
    ENDIF

    READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "ShowPosition" coords
    IF coords=1
    AND NOT isnew>9
        CLEO_CALL posCalc 0 car
        STREAM_CUSTOM_SCRIPT_FROM_LABEL setfade 0
    ENDIF

    IF isnew>9
        GET_LABEL_POINTER TypeRace coords
        WRITE_MEMORY coords 4 1 0

        CLEO_CALL showTTtimers 0
    ELSE
        GET_LABEL_POINTER TypeRace coords
        WRITE_MEMORY coords 4 0 0
    ENDIF

    READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "ShowTimer" offs
    IF offs=1
        isnew=1
        DISPLAY_ONSCREEN_TIMER_WITH_STRING_LOCAL isnew TIMER_UP RCDTTTM
        FREEZE_ONSCREEN_TIMER 0
    ENDIF

    
    SET_PLAYER_CONTROL 0 1
    FREEZE_CAR_POSITION car 0
    GET_VEHICLE_SUBCLASS car offs
    IF offs=VEHICLE_SUBCLASS_PLANE
    OR offs=VEHICLE_SUBCLASS_FPLANE
        SET_CAR_FORWARD_SPEED car 20.0
    ENDIF

    GET_LABEL_POINTER RaceOpts coords
    coords+=8
    READ_MEMORY coords 4 0 offs
    IF NOT offs=0
    AND NOT offs=7
        ALTER_WANTED_LEVEL 0 offs
    ELSE
        IF offs=0
            SET_POLICE_IGNORE_PLAYER 0 1
        ENDIF
        IF offs=7
            
        ENDIF
    ENDIF

    CLEO_CALL startgrid 0 g
   

    //GET_LABEL_POINTER Test coords
    //GET_LAST_CREATED_CUSTOM_SCRIPT coords
    //GOSUB showpos
    GET_LABEL_POINTER Coords coords
    selected=1
    almacen=1
    WHILE almacen=1
        WAIT 0
        STRING_FORMAT idd "%i" selected
        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $idd coords
        SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z radio char angle blip
        IF blip=1
            almacen=0
        ELSE
            selected+=1
        ENDIF
    ENDWHILE
    GET_LABEL_POINTER Coords coords
    selected+=1
    STRING_FORMAT idd "%i" selected
    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $idd coords
    GET_LABEL_POINTER Coords2 offs
    WRITE_MEMORY offs 4 radio 0
    SCAN_STRING $coords "%f %f %f %f" offs x2 y2 z2 radio
    GET_LABEL_POINTER Coords2 offs
    READ_MEMORY offs 4 0 radio
    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
    IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
        CREATE_CHECKPOINT 3 x y z x2 y2 z2 radio checkp
    ELSE
        CREATE_CHECKPOINT 0 x y z x2 y2 z2 radio checkp
    ENDIF
    ADD_BLIP_FOR_COORD x y z blip
    CHANGE_BLIP_COLOUR blip 0

    CLEO_CALL testb 0 g isnew filename menu
    
    GET_LABEL_POINTER RaceOpts coords
    coords+=24
    READ_MEMORY coords 4 0 offs
    IF offs=1
        STORE_WANTED_LEVEL 0 offs
        WRITE_MEMORY coords 4 offs 0
    ENDIF
    i+=1
    //SWITCH_COPS_ON_BIKES 1
    WHILE TRUE
        WAIT 0
        
        IF NOT IS_CHAR_IN_CAR scplayer car
            ADD_BLIP_FOR_CAR car ich
            CHANGE_BLIP_COLOUR ich 0x1762ee
            WHILE NOT IS_CHAR_IN_CAR scplayer car
                WAIT 0
                IF IS_CAR_DEAD car
                OR IS_CHAR_DEAD scplayer
                    REMOVE_BLIP ich
                    GOTO stoprace
                ENDIF
                IF IS_KEY_PRESSED VK_LSHIFT
                AND IS_KEY_JUST_PRESSED VK_KEY_0
                    REMOVE_BLIP ich
                    GOTO stoprace
                ENDIF
                PRINT_FORMATTED_NOW "Get back to your ~b~car~w~!" 100
            ENDWHILE
            REMOVE_BLIP ich
        ENDIF
        
        IF IS_CAR_DEAD car
            IF IS_CAR_IN_WATER car
                PRINT_HELP_FORMATTED "Press ~y~Shift+R~w~ to reset your car, or press ~y~Shift+0~w~ to stop the race."
                WHILE IS_CAR_IN_WATER car
                    WAIT 0
                    IF IS_KEY_PRESSED VK_LSHIFT
                    AND IS_KEY_PRESSED VK_KEY_R
                        CLEO_CALL resetPlayer 0 car i filename x y z
                    ENDIF
                ENDWHILE
            ELSE
                PRINT_FORMATTED_NOW "~r~Your car was destroyed." 1000
                GOTO stoprace
            ENDIF
        ENDIF  

        IF IS_CHAR_DEAD scplayer
            GOTO stoprace
        ENDIF
        
        IF IS_KEY_PRESSED VK_LSHIFT
        AND IS_KEY_PRESSED VK_KEY_R
            CLEO_CALL resetPlayer 0 car i filename x y z
        ENDIF
        
        // IF IS_STRING_EQUAL $idd "STOPNOW" 10 0 "z"
        //     GOTO stoprace
        // ENDIF

        IF IS_KEY_PRESSED VK_LSHIFT
        AND IS_KEY_JUST_PRESSED VK_KEY_0
            GOTO stoprace
        ENDIF
        
        IF IS_KEY_PRESSED 17
        AND IS_KEY_JUST_PRESSED 8
            GOTO stoprace
        ENDIF

        IF LOCATE_CAR_3D car x y z radio radio 30.0 0
            CLEO_CALL checkplayer 0 1 isnew isTrue i checkp blip car checkp blip x y z radio i isTrue
            GET_LABEL_POINTER Opponentschar coords
            coords+=4
            WRITE_MEMORY coords 4 i 0

            
            IF i=-11
                GET_LABEL_POINTER TypeRace coords
                READ_MEMORY coords 4 0 offs

                IF offs=0
                    GOSUB showend
                    i=-10
                ELSE

                    PRINT_HELP_FOREVER RC+&HFX
                    FREEZE_ONSCREEN_TIMER 1
                    WHILE TRUE
                        WAIT 0
                        IF IS_KEY_JUST_PRESSED VK_KEY_Y
                            CLEAR_HELP
                            isnew=11
                            SET_PLAYER_CONTROL 0 0
                            //SET_CAMERA_CONTROL 0
                            DO_FADE 1000 0
                            SET_FADING_COLOUR 0 0 10
                            WAIT 1000
                            FREEZE_ONSCREEN_TIMER 0
                            CLEAR_ONSCREEN_TIMER_LOCAL isnew
                            CLEAR_ONSCREEN_COUNTER_LOCAL isTrue
                            almacen=1
                            DELETE_CHECKPOINT checkp
                            REMOVE_BLIP blip
                            CLEO_CALL deleteObjects 0 list filename 0
                            DELETE_LIST list
                            // DO_FADE 1000 1
                            // WAIT 1000
                            // SET_FADING_COLOUR 0 0 0
                            GOTO testa
                        ENDIF

                        IF IS_KEY_JUST_PRESSED VK_KEY_N
                            CLEAR_HELP
                            GOTO stoprace
                        ENDIF
                    ENDWHILE
                ENDIF
            ENDIF
            IF i=-10
                GOTO stoprace
            ENDIF
        ENDIF
        GET_LABEL_POINTER RaceOpts coords
        coords+=24
        READ_MEMORY coords 4 0 offs
        IF NOT offs=0
            STORE_WANTED_LEVEL 0 selected
            IF NOT offs=selected
                ALTER_WANTED_LEVEL 0 offs
            ENDIF
        ENDIF
        
        // GET_LABEL_POINTER Pos coords
        // READ_MEMORY coords 4 0 offs
        // GET_LABEL_POINTER Opponentsch coords
        // coords+=4
        // READ_MEMORY coords 4 0 selected
        
        // PRINT_FORMATTED_NOW "pos:%i  ch=%i opp=%i" 100 offs i selected
    ENDWHILE
    
//
showend:
    FREEZE_ONSCREEN_TIMER 1
    GET_LABEL_POINTER Opponentschar selected
    READ_MEMORY selected 4 0 offs
    IF offs=0
        IF READ_INT_FROM_INI_FILE $filename "Settings" "Reward" coords
        AND coords>0
            PLAY_MISSION_PASSED_TUNE 1
            PRINT_WITH_NUMBER_BIG M_PASS coords 2000 1
            CHANGE_PLAYER_MONEY 0 CHANGE_MONEY_ADD coords
            WAIT 7000
            i=-10
            RETURN
            // CLEO_RETURN 0 checkp blip x y z radio i isTrue
        ENDIF
        PLAY_MISSION_PASSED_TUNE 1
        PRINT_BIG M_PASSD 2000 1
        WAIT 7000
        i=-10
        RETURN
        // CLEO_RETURN 0 checkp blip x y z radio i isTrue
    ELSE
        GET_LABEL_POINTER Opponentschar selected
        READ_MEMORY selected 4 0 offs
        offs+=1
        IF offs=2
            idd="nd"
        ENDIF
        IF offs=3
            idd="rd"
        ENDIF
        IF offs>3
            idd="th"
        ENDIF
        PRINT_BIG_FORMATTED "You finished %i%s" 2000 1 offs $idd
        WAIT 7000
        i=-10
        RETURN
        // CLEO_RETURN 0 checkp blip x y z radio i isTrue
    ENDIF
    RETURN
setfade:
SET_TEXT_DRAW_BEFORE_FADE 0
USE_TEXT_COMMANDS 1
GOTO showpos
showpos:
    WAIT 0
    GET_LABEL_POINTER Pos coords
    READ_MEMORY coords 4 0 offs
    GET_LABEL_POINTER Helper coords
    

    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 552.0 360.0 100.0 60.0 180.0 0.0 0 0 0 0 0 0 200
    //DRAW_RECT 552.0 360.0 100.0 60.0 0 0 0 50
    DRAW_RECT 502.0 360.0 5.0 60.0 190 190 190 255
    DRAW_RECT 602.0 360.0 5.0 60.0 190 190 190 255
    

    coords+=4
    READ_MEMORY coords 4 0 g
    GET_LABEL_POINTER Istring coords
    STRING_FORMAT coords "%i/%i" offs g
    DRAW_STRING_EXT "Position:" DRAW_EVENT_BEFORE_HUD 510.0 340.0 0.55 1.2 1  2  1 1 0.0 1 255 255 255 255 1 0 0 0 0 255 0 0 0 0 0  //500.0 175.0 old pos
    SWITCH offs
        CASE 1
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 550.0 360.0 0.55 1.2 1  1  0 0 0.0 0 234 190 63 255 1 0 0 0 0 255 0 0 0 0 0 // 534.0 195.0 old pos
            BREAK
        CASE 2
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 550.0 360.0 0.55 1.2 1  1  0 0 0.0 0 150 150 150 255 1 0 0 0 0 255 0 0 0 0 0
            BREAK
        CASE 3
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 550.0 360.0 0.55 1.2 1  1  0 0 0.0 0 205 127 50 255 1 0 0 0 0 255 0 0 0 0 0
            BREAK
        DEFAULT
            DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 550.0 360.0 0.45 1.1 1  1  0 0 0.0 0 255 255 255 255 1 0 0 0 0 255 0 0 0 0 0
            BREAK
    ENDSWITCH
   
    GET_LABEL_POINTER Helper coords
    READ_MEMORY coords 4 0 offs
    IF offs=5
        SET_TEXT_DRAW_BEFORE_FADE 0
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
GOTO showpos
//
/*showposold:
    SET_TEXT_FONT 2
    SET_TEXT_SCALE 0.39 1.6
    SET_TEXT_EDGE 1 0 0 0 255
    
    GET_LABEL_POINTER Pos coords
    READ_MEMORY coords 4 0 offs
    GET_LABEL_POINTER Istring coords
    STRING_FORMAT coords "%i" offs

    ADD_TEXT_LABEL RMFFME0 $coords
    USE_TEXT_COMMANDS 1
    USE_TEXT_COMMANDS 0
    DISPLAY_TEXT 100.0 200.0 RMR-RPP

    SET_TEXT_FONT 2
    SET_TEXT_SCALE 0.39 1.6
    IF offs=1
        SET_TEXT_COLOUR 150 100 0 255
    ENDIF
    IF offs=2
        SET_TEXT_COLOUR 150 150 150 255
    ENDIF
    IF offs=3
        SET_TEXT_COLOUR 205 127 50 255
    ENDIF
    SET_TEXT_EDGE 1 0 0 0 255
    DISPLAY_TEXT 80.0 230.0 RMFFME0
    RETURN*/
//
customcat:
    GET_LABEL_POINTER Customcate selected
    selected+=40
    READ_MEMORY selected 4 0 offs
    STRING_FORMAT idd "CUSTOM_CAT%i" offs
    GOSUB cantidadautos
    GOSUB retornarauto
    RETURN
//
cantidadautos:
        selected=1
        GET_LABEL_POINTER Istring offs
        STRING_FORMAT offs "%i" selected
        WHILE READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" $idd $offs filen
            WAIT 0
            selected+=1
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" selected
        ENDWHILE
RETURN
//
retornarauto:
        IF NOT selected=0
            CLEO_CALL getRandomSeed 0 coords
            GENERATE_RANDOM_INT_IN_RANGE_WITH_SEED coords 1 selected selected
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" selected
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" $idd $offs selected
            REQUEST_MODEL selected
            WHILE NOT HAS_MODEL_LOADED selected
                WAIT 0
            ENDWHILE
            RETURN
        ELSE
            GET_CAR_MODEL car selected
            REQUEST_MODEL selected
            WHILE NOT HAS_MODEL_LOADED selected
                WAIT 0
            ENDWHILE
            RETURN
        ENDIF

//
randomcat:
    GET_LABEL_POINTER Dumper2 selected
    READ_MEMORY selected 4 0 offs
    
    SWITCH offs
        CASE 1
            idd="Exotics"
            BREAK
        CASE 2
            idd="Tuners"
            BREAK
        CASE 3
            idd="Muscle"
            BREAK
        CASE 4
            idd="Lowriders"
            BREAK
        CASE 5
            idd="Coupes"
            BREAK
        CASE 6
            idd="Sedans"
            BREAK
        CASE 7
            idd="Offroad"
            BREAK
        CASE 8
            idd="Bikes"
            BREAK
        CASE 9
            idd="Racecars"
            BREAK
        CASE 11
            idd="Planes"
            BREAK
        CASE 12
            idd="Helicopters"
            BREAK
        CASE 13
            idd="Boats"
            BREAK
        DEFAULT
            idd="Mixed"
            BREAK
    ENDSWITCH
    IF NOT IS_STRING_EQUAL $idd "Mixed" 7 0 "z"
        GOSUB cantidadautos
        //filen
        //selected-=1
        GOSUB retornarauto
    ELSE
        CLEO_CALL getRandomSeed 0 coords
        GENERATE_RANDOM_INT_IN_RANGE_WITH_SEED coords 1 5000 selected
        WHILE NOT IS_THIS_MODEL_A_CAR selected
            WAIT 0
            CLEO_CALL getRandomSeed 0 coords
            PRINT_FORMATTED_NOW "%i %i" 500 selected coords
            GENERATE_RANDOM_INT_IN_RANGE_WITH_SEED coords 1 5000 selected
        ENDWHILE
        GENERATE_RANDOM_INT_IN_RANGE_WITH_SEED coords 1 500 offs
        IF offs < 5
            selected=471
        ENDIF
        REQUEST_MODEL selected
        WHILE NOT HAS_MODEL_LOADED selected
            WAIT 0
        ENDWHILE
        RETURN
    ENDIF
    RETURN
//
getmods:
    offs=0
    WHILE NOT offs=16
        WAIT 0
        GET_AVAILABLE_VEHICLE_MOD car offs almacen
        GET_LABEL_POINTER VehMods coords
        IF offs=0
            WRITE_MEMORY coords 4 almacen 0
        ELSE
            filen=4*offs
            coords+=filen
            WRITE_MEMORY coords 4 almacen 0
        ENDIF
        offs+=1
    ENDWHILE
RETURN
//
setmods:
    
    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "AnyColourForCars" coords
    IF coords=2
        GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
        GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
        CHANGE_CAR_COLOUR car offs coords
        GENERATE_RANDOM_INT_IN_RANGE 0 126 offs
        GENERATE_RANDOM_INT_IN_RANGE 0 126 coords
        SET_EXTRA_CAR_COLOURS car offs coords
    ENDIF

    offs=0
    WHILE NOT offs=16
        WAIT 0
        GET_LABEL_POINTER VehMods coords
        IF offs=0
            READ_MEMORY coords 4 0 almacen
        ELSE
            filen=4*offs
            coords+=filen
            READ_MEMORY coords 4 0 almacen  
        ENDIF

        IF NOT almacen=-1
            REQUEST_VEHICLE_MOD almacen
            WHILE NOT HAS_VEHICLE_MOD_LOADED almacen
                WAIT 0
            ENDWHILE
            GENERATE_RANDOM_INT_IN_RANGE 1 10 coords
            IF coords<6
                GET_CAR_MODEL testcar filen
                PRINT_FORMATTED_NOW "%i %i" 200 filen almacen 
                
                ADD_VEHICLE_MOD testcar almacen filen
            ENDIF
            MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED almacen
        ENDIF
        offs+=1
    ENDWHILE
RETURN
//
setcustomcolours:
    GET_LABEL_POINTER Coords coords
    STRING_FORMAT coords "Colour1Car%i" g
    IF NOT READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        offs=-1
    ENDIF
    STRING_FORMAT coords "Colour2Car%i" g
    IF NOT READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
        selected=-1
    ENDIF
    IF NOT offs=-1
    AND NOT selected=-1
        CHANGE_CAR_COLOUR testcar offs selected
    ENDIF
    STRING_FORMAT coords "Colour3Car%i" g
    IF NOT READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        offs=-1
    ENDIF
    STRING_FORMAT coords "Colour4Car%i" g
    IF NOT READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords selected
        selected=-1
    ENDIF
    IF NOT offs=-1
    AND NOT selected=-1
        SET_EXTRA_CAR_COLOURS testcar offs selected
    ENDIF
    RETURN
//
setcustompaintjob:
    GET_LABEL_POINTER Coords coords
    STRING_FORMAT coords "Paintjob%i" g
    IF READ_STRING_FROM_INI_FILE $filename "Vehicles ids" $coords idd
    OR isnew=2
    OR isnew=1
    OR isnew=4
        IF IS_STRING_EQUAL $idd "RANDOM" 7 0 "t"
        OR isnew=2
        OR isnew=1
        OR isnew=4
            GET_NUM_AVAILABLE_PAINTJOBS testcar selected
            GENERATE_RANDOM_INT_IN_RANGE 0 selected offs
            IF isnew=2
            OR isnew=1
                GENERATE_RANDOM_INT_IN_RANGE -1 selected offs
            ENDIF
            GIVE_VEHICLE_PAINTJOB testcar offs
        ELSE
            SCAN_STRING $idd "%i" offs selected
            GIVE_VEHICLE_PAINTJOB testcar selected
        ENDIF
    ENDIF
RETURN

setcustommods:
    GET_CAR_MODEL testcar selected
    //hood
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Hood%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //vents
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Vent%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Spoiler
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Spoiler%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //sideskirt
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "SideSkirt%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected =3
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                LOAD_ALL_MODELS_NOW
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Fbullbar
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "FBullbarr%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Rbullbar
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "RBullbar%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Lights
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Lights%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Roof
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Roof%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Nitro
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Nitro%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
            REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Wheels
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Wheels%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //Exhaust
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "Exhaust%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //FBumper
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "FBumper%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //RBumper
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "RBumper%i" g
        IF READ_INT_FROM_INI_FILE $filename "Vehicles ids" $coords offs
        AND NOT offs=-1
            GET_VEHICLE_MOD_TYPE offs selected
            IF selected >0
            AND selected<16
                REQUEST_VEHICLE_MOD offs
                WHILE NOT HAS_MODEL_LOADED offs
                    WAIT 0
                ENDWHILE
                ADD_VEHICLE_MOD testcar offs coords
            ENDIF
        ENDIF
        MARK_VEHICLE_MOD_AS_NO_LONGER_NEEDED offs
    //ya
RETURN 
//

//

//

stoprace:
    GET_LABEL_POINTER Helper coords
    WRITE_MEMORY coords 4 5 1
    SET_PLAYER_CONTROL 0 0
    DO_FADE 1000 0
    SET_FADING_COLOUR 0 0 10
    WAIT 1000
    
    FREEZE_ONSCREEN_TIMER 0
    GET_LABEL_POINTER RaceOpts coords
    coords+=32
    READ_MEMORY coords 4 0 offs
    IF offs=1
        WRITE_MEMORY 0x53BFBD 1 0xE8 1
        WRITE_MEMORY 0x53BFBE 1 0x4E 1
        WRITE_MEMORY 0x53BFBF 1 0x0F 1
        WRITE_MEMORY 0x53BFC0 1 0xFF 1
        WRITE_MEMORY 0x53BFC1 1 0xFF 1
    ENDIF
    RELEASE_WEATHER
    CLEAR_WANTED_LEVEL 0
    IF NOT IS_CHAR_ON_FOOT scplayer
        GET_LABEL_POINTER IsPlayerCar offs
        READ_MEMORY offs 4 0 selected
        GET_CAR_CHAR_IS_USING scplayer car
        SET_CAR_CAN_BE_VISIBLY_DAMAGED car 1
        SET_CAR_PROOFS car 0 0 0 0 0
        IF selected=-1
            GET_CAR_CHAR_IS_USING scplayer car
            TASK_LEAVE_CAR_IMMEDIATELY scplayer car
            WHILE IS_CHAR_IN_CAR scplayer car
                WAIT 0
            ENDWHILE
            MARK_CAR_AS_NO_LONGER_NEEDED car
            DELETE_CAR car
        ENDIF
    ENDIF
    GET_CHAR_COORDINATES scplayer x y z
    CLEAR_AREA x y z 2000000.0 1
    CLEAR_ONSCREEN_TIMER_LOCAL isnew
    CLEAR_ONSCREEN_COUNTER_LOCAL isTrue
    almacen=1
    DELETE_CHECKPOINT checkp
    REMOVE_BLIP blip
    WHILE almacen <= g
        GET_LABEL_POINTER Opponents offs
        selected=almacen*4
        offs+=selected
        READ_MEMORY offs 4 0 testcar
        IF DOES_VEHICLE_EXIST testcar
            GET_EXTENDED_CAR_VAR testcar "RaceC++" 9 char
            IF DOES_CHAR_EXIST char
                MARK_CHAR_AS_NO_LONGER_NEEDED char
                DELETE_CHAR char
            ENDIF
            MARK_CAR_AS_NO_LONGER_NEEDED testcar
            DELETE_CAR testcar
        ENDIF
        almacen+=1
    ENDWHILE
    GET_LABEL_POINTER PlayerInt coords
    READ_MEMORY coords 4 0 offs
    SET_AREA_VISIBLE offs
    SET_CHAR_AREA_VISIBLE scplayer offs
    IF IS_CHAR_IN_ANY_CAR scplayer
        GET_CAR_CHAR_IS_USING scplayer car
        SET_VEHICLE_AREA_VISIBLE car offs
    ENDIF
    GET_LABEL_POINTER PlayerCoords coords
    SCAN_STRING $coords "%f %f %f" offs x y z
    SET_CHAR_COORDINATES scplayer x y z
    SET_PED_DENSITY_MULTIPLIER 1.0
    SET_CAR_DENSITY_MULTIPLIER 1.0
    SET_EVERYONE_IGNORE_PLAYER 0 0
    SET_POLICE_IGNORE_PLAYER 0 0
    SET_WEATHER_TO_APPROPRIATE_TYPE_NOW
    WRITE_MEMORY 0x863984 4 0.008 1
    CLEO_CALL deleteObjects 0 list filename 0
    DELETE_LIST list
    DO_FADE 1000 1
    WAIT 1000
    SET_FADING_COLOUR 0 0 0
    SET_PLAYER_CONTROL 0 1
    GET_LABEL_POINTER Helper coords
    WRITE_MEMORY coords 4 0 1
    coords+=4
    WRITE_MEMORY coords 4 -1 1
    coords=0
    GOTO mainmenu
}



{
    LVAR_INT g timer filename laps ich offs almacen selected coords car char esfera filen scplayer numero
    LVAR_FLOAT x y z angle radio
    testb:
    GET_LABEL_POINTER Dumper filename
    //STRING_FORMAT filename "Cleo/Race Creator++/Races/%s" $filen
    WHILE TRUE
        WAIT 0
        almacen=1
        IF almacen=10
            GET_PLAYER_CHAR 0 scplayer
            GET_CAR_CHAR_IS_USING scplayer car
        ENDIF
        GET_LABEL_POINTER Helper coords
        coords+=4
        READ_MEMORY coords 4 0 numero

        WHILE almacen<numero
            WAIT 0
            
            GET_LABEL_POINTER Opponents esfera
            offs=almacen*4
            esfera+=offs
            READ_MEMORY esfera 4 0 car
            GET_LABEL_POINTER Coords coords
            STREAM_CUSTOM_SCRIPT_FROM_LABEL oppTrayecto filename car timer
            
            // GET_LAST_CREATED_CUSTOM_SCRIPT coords
            // SET_SCRIPT_VAR coords 0 car
            almacen+=1
        ENDWHILE
 
        CLEO_RETURN 0 
           
    ENDWHILE


}

{
LVAR_INT filename car timer almacen g ich offs selected coords char esfera filen scplayer tipo 
LVAR_FLOAT x y z angle radio x2 y2 z2 traccion
oppTrayecto:
    
    almacen=0
    
    IF almacen=-1289
        GET_RANDOM_CAR_IN_SPHERE_NO_SAVE_RECURSIVE 100.0 1000.0 100.0 10.0 1 1 car
        GET_RANDOM_CHAR_IN_SPHERE 100.0 100.0 10.0 10.0 1 1 1 char
    ENDIF
    GET_LABEL_POINTER Coords offs
    READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" offs
    SCAN_STRING $offs "%f %f %f %f %i %f %i %f %f" selected x y z radio filen angle esfera traccion y2
    
    WHILE TRUE
        WAIT 0
        IF DOES_VEHICLE_EXIST car
            GET_VEHICLE_SUBCLASS car offs
            IF IS_CAR_IN_WATER car
            AND NOT offs=VEHICLE_SUBCLASS_BOAT
                GET_EXTENDED_CAR_VAR car "RaceC++" 10 selected
                IF NOT selected=-1
                    GET_LABEL_POINTER Istring ich
                    GOSUB resetcar
                ENDIF
            ENDIF
            IF NOT IS_CAR_DEAD car
                IF GET_EXTENDED_CAR_VAR car "RaceC++" 9 char
                    IF IS_CHAR_DEAD char
                        MARK_CHAR_AS_NO_LONGER_NEEDED char
                    ENDIF
                ENDIF

                GET_EXTENDED_CAR_VAR car "RaceC++" 11 selected
                IF NOT selected=-1
                    GET_LABEL_POINTER Istring ich
                    IF IS_CAR_UPSIDEDOWN car
                        IF NOT IS_CAR_ON_SCREEN car
                            GOSUB resetcar
                        ENDIF
                    ENDIF

                    GET_CAR_SPEED car angle
                    //GET_EXTENDED_CAR_VAR car "RaceC++" 10 selected
                    //GET_EXTENDED_CAR_VAR car "RaceC++" 11 offs
                    
                    IF angle<1.0
                        IF NOT IS_CAR_ON_SCREEN car                        
                            GOSUB resetcar
                        ENDIF
                    ENDIF

                    GET_EXTENDED_CAR_VAR car "RaceC++" 1 x
                    GET_EXTENDED_CAR_VAR car "RaceC++" 2 y
                    GET_EXTENDED_CAR_VAR car "RaceC++" 3 z
                    GET_EXTENDED_CAR_VAR car "RaceC++" 4 radio

                    IF LOCATE_CAR_3D car x y z radio radio 30.0 0
                        GET_EXTENDED_CAR_VAR car "RaceC++" 12 ich
                        GET_LABEL_POINTER Coords2 offs
                        GET_EXTENDED_CAR_VAR car "RaceC++" 11 coords
                        GET_LABEL_POINTER Istring selected
                        STRING_FORMAT selected "%i" coords
                        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected offs
                        SCAN_STRING $offs "%f %f %f %f %i %f %i %f %f" selected x y z radio filen angle esfera x2 y2

                        SET_EXTENDED_CAR_VAR car "RaceC++" 5 x
                        SET_EXTENDED_CAR_VAR car "RaceC++" 6 y
                        SET_EXTENDED_CAR_VAR car "RaceC++" 7 z
                        SET_EXTENDED_CAR_VAR car "RaceC++" 8 filen
                        SET_EXTENDED_CAR_VAR car "RaceC++" 13 y2

                        IF NOT coords=-1
                        AND NOT coords=-2
                            coords+=1
                            SET_EXTENDED_CAR_VAR car "RaceC++" 11 coords
                        ENDIF
                        
                        GET_LABEL_POINTER Istring selected
                        STRING_FORMAT selected "%i" coords
                        GET_LABEL_POINTER Coords coords
                        IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                        AND NOT IS_STRING_EQUAL $coords "DELETED" 9 0 "p"
                            SCAN_STRING $coords "%f %f %f %f %i %f %i %f" selected x y z radio filen angle tipo traccion
                            GET_LABEL_POINTER Coords2 offs

                            IF ich=2
                                
                                WHILE IS_AREA_OCCUPIED x y z x y z 0 1 0 0 0
                                    z+=0.5
                                ENDWHILE
                                SET_CAR_COORDINATES car x y z
                                GET_LABEL_POINTER Coords coords
                                GET_LABEL_POINTER Istring selected

                                GET_EXTENDED_CAR_VAR car "RaceC++" 11 offs
                                offs+=1
                                STRING_FORMAT selected "%i" offs
                                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                                SCAN_STRING $coords "%f %f" selected x2 y2
                                radio+=1000.0
                                TURN_CAR_TO_FACE_COORD car x2 y2
                                SET_CAR_FORWARD_SPEED car angle
                            ENDIF
                            
                            SET_EXTENDED_CAR_VAR car "RaceC++" 1 x
                            SET_EXTENDED_CAR_VAR car "RaceC++" 2 y
                            SET_EXTENDED_CAR_VAR car "RaceC++" 3 z
                            SET_EXTENDED_CAR_VAR car "RaceC++" 4 radio
                            SET_EXTENDED_CAR_VAR car "RaceC++" 12 tipo


                            IF DOES_VEHICLE_EXIST car
                                SET_CAR_TRACTION car traccion
                                
                                GET_DRIVER_OF_CAR car char
                                GET_VEHICLE_SUBCLASS car offs
                                IF offs=VEHICLE_SUBCLASS_HELI
                                OR offs=VEHICLE_SUBCLASS_FHELI
                                    HELI_GOTO_COORDS car x y z 0.0 z
                                ELSE
                                    GET_CAR_MODEL car selected
                                    IF offs=VEHICLE_SUBCLASS_PLANE
                                    AND NOT selected=539
                                        PLANE_GOTO_COORDS car x y z 0.0 z
                                    ELSE 
                                        IF offs=VEHICLE_SUBCLASS_BOAT
                                            BOAT_GOTO_COORDS car x y z
                                            SET_BOAT_CRUISE_SPEED car angle
                                        ELSE                       
                                            IF filen=0
                                                CLEAR_CHAR_TASKS char
                                                CAR_GOTO_COORDINATES_RACING car x y z
                                                SET_CAR_CRUISE_SPEED car angle
                                                SET_CAR_DRIVING_STYLE car 2
                                                SET_CAR_CAN_GO_AGAINST_TRAFFIC car 1
                                                
                                            ELSE
                                                // CAR_GOTO_COORDINATES car x2 y2 z2
                                                // SET_CAR_CRUISE_SPEED car angle
                                                // SET_CAR_DRIVING_STYLE car 2
                                                //SET_CAR_STRAIGHT_LINE_DISTANCE testcar 250
                                                CLEAR_CHAR_TASKS char
                                                TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2

                                            ENDIF
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ELSE
                            READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" selected
                            IF selected=0
                                GET_LABEL_POINTER Opponentschar offs
                                READ_MEMORY offs 4 0 selected
                                selected+=1
                                WRITE_MEMORY offs 4 selected 0
                                SET_EXTENDED_CAR_VAR car "RaceC++" 11 -1
                                CAR_SET_IDLE car
                            ELSE
                                GET_LABEL_POINTER Totlaps coords
                                READ_MEMORY coords 4 0 filen
                                GET_EXTENDED_CAR_VAR car "RaceC++" 11 offs
                                IF offs=-2
                                    SET_EXTENDED_CAR_VAR car "RaceC++" 11 -1                              
                                    GET_LABEL_POINTER Opponentschar offs
                                    READ_MEMORY offs 4 0 selected
                                    selected+=1
                                    WRITE_MEMORY offs 4 selected 0
                                    CAR_SET_IDLE car
                                ELSE
                                    GET_EXTENDED_CAR_VAR car "RaceC++" 10 esfera  
                                    IF IS_INT_LVAR_EQUAL_TO_INT_LVAR esfera filen
                                        WAIT 0
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 11 -2   
                                    ELSE
                                        esfera+=1
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 10 esfera
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 11 0   
                                    ENDIF
                                    GET_LABEL_POINTER Coords coords
                                    READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords

                                        SCAN_STRING $coords "%f %f %f %f %i %f %i" selected x y z radio filen angle tipo

                                        IF ich=2
                                            
                                            WHILE IS_AREA_OCCUPIED x y z x y z 0 1 0 0 0
                                                z+=0.5
                                            ENDWHILE
                                            SET_CAR_COORDINATES car x y z
                                            GET_LABEL_POINTER Coords coords
                                            READ_STRING_FROM_INI_FILE $filename "Checkpoints" "1" coords
                                            SCAN_STRING $coords "%f %f" selected x2 y2
                                            radio+=1000.0
                                            TURN_CAR_TO_FACE_COORD car x2 y2
                                            SET_CAR_FORWARD_SPEED car angle
                                        ENDIF
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 1 x
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 2 y
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 3 z
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 4 radio
                                        SET_EXTENDED_CAR_VAR car "RaceC++" 12 tipo

                                        GET_VEHICLE_SUBCLASS car offs
                                        SET_CAR_TRACTION car traccion
                                        IF offs=VEHICLE_SUBCLASS_HELI
                                        OR offs=VEHICLE_SUBCLASS_FHELI
                                            HELI_GOTO_COORDS car x y z 0.0 z
                                        ELSE
                                            GET_CAR_MODEL car selected
                                            IF offs=VEHICLE_SUBCLASS_PLANE
                                            AND NOT selected=539
                                                PLANE_GOTO_COORDS car x y z 0.0 z
                                            ELSE  
                                                IF offs=VEHICLE_SUBCLASS_BOAT
                                                    BOAT_GOTO_COORDS car x y z
                                                    SET_BOAT_CRUISE_SPEED car angle
                                                ELSE                     
                                                    IF filen=0
                                                        GET_DRIVER_OF_CAR car char
                                                        CLEAR_CHAR_TASKS char
                                                        CAR_GOTO_COORDINATES_RACING car x y z
                                                        SET_CAR_CRUISE_SPEED car angle
                                                    ELSE
                                                        GET_DRIVER_OF_CAR car char
                                                        CLEAR_CHAR_TASKS char
                                                        TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2
                                                        TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2 
                                                        TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2   
                                                        //CAR_GOTO_COORDINATES testcar x2 y2 z2
                                                        //SET_CAR_CRUISE_SPEED testcar angle
                                                        //SET_CAR_DRIVING_STYLE testcar 2
                                                        //SET_CAR_STRAIGHT_LINE_DISTANCE testcar 200
                                                    ENDIF 
                                                ENDIF
                                            ENDIF
                                        ENDIF                                   
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ELSE
                IF NOT IS_CAR_IN_WATER car
                    MARK_CAR_AS_NO_LONGER_NEEDED car
                ENDIF
            ENDIF
        ENDIF
        GET_LABEL_POINTER Helper coords
        READ_MEMORY coords 4 0 offs
        IF offs=5
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF
    ENDWHILE
resetcar:
    GET_LABEL_POINTER Coords2 offs
    GET_EXTENDED_CAR_VAR car "RaceC++" 11 coords
    GET_LABEL_POINTER Istring selected
    STRING_FORMAT selected "%i" coords
    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected offs
    SCAN_STRING $offs "%f %f %f %f %i %f %i %f %f" selected x y z radio filen angle esfera x2 y2

    GET_EXTENDED_CAR_VAR car "RaceC++" 5 x
    GET_EXTENDED_CAR_VAR car "RaceC++" 6 y
    GET_EXTENDED_CAR_VAR car "RaceC++" 7 z
    GET_EXTENDED_CAR_VAR car "RaceC++" 8 filen
    GET_EXTENDED_CAR_VAR car "RaceC++" 13 y2
    
    IF NOT IS_AREA_OCCUPIED x y z x y z 0 1 0 0 0
        z+=1.0
        SET_CAR_COORDINATES car x y z
        GET_EXTENDED_CAR_VAR car "RaceC++" 1 x
        GET_EXTENDED_CAR_VAR car "RaceC++" 2 y
        GET_EXTENDED_CAR_VAR car "RaceC++" 3 z
        TURN_CAR_TO_FACE_COORD car x y

        IF NOT IS_FLOAT_LVAR_EQUAL_TO_NUMBER y2 0.0
            SET_CAR_ROLL car y2
        ENDIF

        IF DOES_VEHICLE_EXIST car
            SET_CAR_TRACTION car traccion
            
            GET_DRIVER_OF_CAR car char
            GET_VEHICLE_SUBCLASS car offs
            IF offs=VEHICLE_SUBCLASS_HELI
            OR offs=VEHICLE_SUBCLASS_FHELI
                HELI_GOTO_COORDS car x y z 0.0 z
            ELSE
                GET_CAR_MODEL car selected
                IF offs=VEHICLE_SUBCLASS_PLANE
                AND NOT selected=539
                    PLANE_GOTO_COORDS car x y z 0.0 z
                ELSE 
                    IF offs=VEHICLE_SUBCLASS_BOAT
                        BOAT_GOTO_COORDS car x y z
                        SET_BOAT_CRUISE_SPEED car angle
                    ELSE                       
                        IF filen=0
                            CLEAR_CHAR_TASKS char
                            CAR_GOTO_COORDINATES_RACING car x y z
                            SET_CAR_CRUISE_SPEED car angle
                            SET_CAR_DRIVING_STYLE car 2
                            SET_CAR_CAN_GO_AGAINST_TRAFFIC car 1
                            
                        ELSE
                            // CAR_GOTO_COORDINATES car x2 y2 z2
                            // SET_CAR_CRUISE_SPEED car angle
                            // SET_CAR_DRIVING_STYLE car 2
                            //SET_CAR_STRAIGHT_LINE_DISTANCE testcar 250
                            GET_DRIVER_OF_CAR car char
                            CLEAR_CHAR_TASKS char
                            TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2
                            TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2 
                            TASK_CAR_DRIVE_TO_COORD char car x y z angle 2 0 2   

                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
                            ENDIF
        SET_CAR_FORWARD_SPEED car 20.0
    //ELSE
        //PRINT_FORMATTED_NOW "area occ" 1000
    ENDIF
RETURN

}

{
    LVAR_INT car i filename 
    LVAR_FLOAT x y z x2 y2 z2 radio angle traction roll selected
    LVAR_INT offs coords beh type coords2

    resetPlayer:
    GET_LABEL_POINTER Dumper filename
    GET_LABEL_POINTER Coords coords
    GET_LABEL_POINTER Istring offs

    IF NOT i=0
        i-=2 
        STRING_FORMAT offs "%i" i
        i+=2
    ELSE
        GET_LABEL_POINTER TotCheck coords2
        READ_MEMORY coords2 4 0 selected
        STRING_FORMAT offs "%i" selected
    ENDIF

    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
    SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" offs x2 y2 z2 radio beh angle type traction roll
    IF NOT IS_AREA_OCCUPIED x2 y2 z2 x2 y2 z2 0 1 0 0 0
        DO_FADE 50 FADE_OUT //0 ENTRAR A FADE
        SET_CAR_COORDINATES car x2 y2 z2
        IF NOT roll=0.0
            SET_CAR_ROLL car roll
        ENDIF
        TURN_CAR_TO_FACE_COORD car x y
        DO_FADE 50 FADE_IN
        
    ENDIF
    WAIT 50
    
    CLEO_RETURN 0
}

{
    LVAR_INT tipe isnew isTrue i checkp blip car filename esfera selected offs scplayer coords g aux filen almacen char
    LVAR_FLOAT x y z x2 y2 z2 angle radio
    LVAR_TEXT_LABEL16 idd
    LVAR_TEXT_LABEL textaux
    checkplayer:
        GET_PLAYER_CHAR 0 scplayer
        GET_LABEL_POINTER Dumper filename
        // IF IS_KEY_PRESSED 17
        // AND IS_KEY_JUST_PRESSED 8
        //     i=-10
        //     CLEO_RETURN 0 checkp blip x y z radio i isTrue
        // ENDIF
        GET_LABEL_POINTER Coords coords
        GET_LABEL_POINTER Opponentslap offs
        READ_MEMORY offs 4 0 selected
        
        IF selected=-1
            GET_LABEL_POINTER LapTime offs
            READ_MEMORY offs 4 0 filen

            coords=isnew-filen
            filen=coords

            //minutos
            offs=coords/60000

            selected=offs*60000
            filen-=selected

            //segundos
            tipe=filen/1000
            selected=tipe*1000
            filen-=selected

            IF tipe<10
                STRING_FORMAT textaux "0%i" tipe
            ELSE
                STRING_FORMAT textaux "%i" tipe
            ENDIF

            IF filen<10
                STRING_FORMAT idd "00%i" filen
            ELSE
                IF filen<100
                    STRING_FORMAT idd "0%i" filen
                ELSE
                    STRING_FORMAT idd "%i" filen
                ENDIF
            ENDIF

            IF offs<10
                PRINT_FORMATTED_NOW "Lap time: ~b~0%i:%s.%s" 10000 offs $textaux $idd
            ELSE
                PRINT_FORMATTED_NOW "Lap time: ~b~%i:%s.%s" 10000 offs $textaux $idd
            ENDIF
            
            GET_LABEL_POINTER TypeRace coords
            READ_MEMORY coords 4 0 aux
            IF aux=1
                WAIT 1000
                CLEO_CALL updateRecord 0 offs tipe filen
            ENDIF

            GET_LABEL_POINTER LapTime filen
            WRITE_MEMORY filen 4 isnew 0
            i=-11
            CLEO_RETURN 0 checkp blip x y z radio i isTrue
        ELSE
            GET_LABEL_POINTER Coords coords
            GET_LABEL_POINTER Istring offs
            i-=1
            STRING_FORMAT offs "%i" i
            READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
            SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z radio char angle aux
            i+=1
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" i
            IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
            AND NOT IS_STRING_EQUAL $coords "DELETED" 7 0 "m"
                SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z radio char angle selected
                WHILE selected=0
                    i+=1
                    GET_LABEL_POINTER Istring offs
                    STRING_FORMAT offs "%i" i
                    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
                    SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z radio char angle selected
                ENDWHILE 

                IF aux=2
                    WHILE IS_AREA_OCCUPIED x y z x y z 0 1 0 0 0
                        z+=0.5
                    ENDWHILE
                    SET_CAR_COORDINATES car x y z
                    i+=1
                    GET_LABEL_POINTER Istring offs
                    STRING_FORMAT offs "%i" i
                    IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
                    AND NOT IS_STRING_EQUAL $coords "DELETED" 10 0 "z"
                        SCAN_STRING $coords "%f %f %f" offs x2 y2 z2
                    ELSE
                        READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
                        SCAN_STRING $coords "%f %f %f" offs x2 y2 z2
                    ENDIF
                    i-=1
                    TURN_CAR_TO_FACE_COORD car x2 y2
                    SET_CAR_FORWARD_SPEED car angle
                ENDIF

                DELETE_CHECKPOINT checkp
                REMOVE_BLIP blip
                GET_LABEL_POINTER Opponentslap offs
                READ_MEMORY offs 4 0 selected
                GET_LABEL_POINTER Totlaps offs
                READ_MEMORY offs 4 0 esfera
                IF i=0
                AND IS_INT_LVAR_EQUAL_TO_INT_LVAR esfera selected
                    CREATE_CHECKPOINT 1 x y z x y z radio checkp
                    ADD_BLIP_FOR_COORD x y z blip
                    CHANGE_BLIP_COLOUR blip 1
                    REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_CHECKPOINT_RED
                    GET_LABEL_POINTER Opponentslap offs
                    WRITE_MEMORY offs 4 -1 0
                    IF aux=2
                        radio+=100.0
                        CHANGE_BLIP_COLOUR blip 4
                    ENDIF
                    CLEO_RETURN 0 checkp blip x y z radio i isTrue 
                ENDIF
                IF i=1
                    GET_LABEL_POINTER Totlaps offs
                    READ_MEMORY offs 4 0 coords
                    GET_LABEL_POINTER Opponentslap selected
                    READ_MEMORY selected 4 0 esfera
                    esfera+=1
                    isTrue+=1
                    IF esfera=coords
                        GET_LABEL_POINTER LapTime offs
                        READ_MEMORY offs 4 0 filen
                        coords=isnew-filen
                        filen=coords

                        //minutos
                        offs=coords/60000

                        selected=offs*60000
                        filen-=selected

                        //segundos
                        tipe=filen/1000
                        selected=tipe*1000
                        filen-=selected

                        IF tipe<10
                            STRING_FORMAT textaux "0%i" tipe
                        ELSE
                            STRING_FORMAT textaux "%i" tipe
                        ENDIF

                        IF filen<10
                            STRING_FORMAT idd "00%i" filen
                        ELSE
                            IF filen<100
                                STRING_FORMAT idd "0%i" filen
                            ELSE
                                STRING_FORMAT idd "%i" filen
                            ENDIF
                        ENDIF

                        IF offs<10
                            PRINT_FORMATTED_NOW "Lap time: ~b~0%i:%s.%s~n~~y~FINAL LAP!" 3000 offs $textaux $idd
                        ELSE
                            PRINT_FORMATTED_NOW "Lap time: ~b~%i:%s.%s~n~~y~FINAL LAP!" 3000 offs $textaux $idd
                        ENDIF

                        GET_LABEL_POINTER LapTime filen
                        WRITE_MEMORY filen 4 isnew 0
                        //PRINT_FORMATTED_NOW "~b~FINAL LAP!" 1000
                        
                        REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_PART_MISSION_COMPLETE
                    ELSE
                        GET_LABEL_POINTER LapTime offs
                        READ_MEMORY offs 4 0 filen
                        
                        coords=isnew-filen
                        filen=coords

                        //minutos
                        offs=coords/60000

                        selected=offs*60000
                        filen-=selected

                        //segundos
                        tipe=filen/1000
                        selected=tipe*1000
                        filen-=selected

                        IF tipe<10
                            STRING_FORMAT textaux "0%i" tipe
                        ELSE
                            STRING_FORMAT textaux "%i" tipe
                        ENDIF

                        IF filen<10
                            STRING_FORMAT idd "00%i" filen
                        ELSE
                            IF filen<100
                                STRING_FORMAT idd "0%i" filen
                            ELSE
                                STRING_FORMAT idd "%i" filen
                            ENDIF
                        ENDIF

                        IF offs<10
                            PRINT_FORMATTED_NOW "Lap time: ~b~0%i:%s.%s" 3000 offs $textaux $idd//ver si anda
                        ELSE
                            PRINT_FORMATTED_NOW "Lap time: ~b~%i:%s.%s" 3000 offs $textaux $idd//ver si anda
                        ENDIF
                        
                        selected=filen  //milisegundos
                        coords=tipe //segundos
                        
                        GET_LABEL_POINTER LapTime filen
                        WRITE_MEMORY filen 4 isnew 0
                        //PRINT_FORMATTED_NOW "Lap %i/%i" 1000 esfera coords 
                        REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_PART_MISSION_COMPLETE
                        
                        WAIT 1000
                    ENDIF
                    GET_LABEL_POINTER TypeRace tipe
                    READ_MEMORY tipe 4 0 aux
                    IF aux=1
                        WAIT 1000
                        CLEO_CALL updateRecord 0 offs coords selected
                    ENDIF
                    GET_LABEL_POINTER Opponentslap selected
                    WRITE_MEMORY selected 4 esfera 0
                ENDIF
                REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_CHECKPOINT_RED
                GET_LABEL_POINTER Istring offs
                selected=i+1
                STRING_FORMAT offs "%i" selected
                GET_LABEL_POINTER Coords coords
                IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
                AND NOT IS_STRING_EQUAL $coords "DELETED" 7 0 "p"
                    GET_LABEL_POINTER Coords2 offs
                    WRITE_MEMORY offs 4 radio 0
                    SCAN_STRING $coords "%f %f %f %f" offs x2 y2 z2 radio
                    GET_LABEL_POINTER Coords2 offs
                    READ_MEMORY offs 4 0 radio
                    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                    IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                        CREATE_CHECKPOINT 4 x y z x2 y2 z2 radio checkp
                    ELSE
                        CREATE_CHECKPOINT 0 x y z x2 y2 z2 radio checkp
                    ENDIF
                    ADD_BLIP_FOR_COORD x y z blip
                    CHANGE_BLIP_COLOUR blip 0
                    i+=1
                ELSE
                    READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" selected
                    IF selected=0
                        SCAN_STRING $coords "%f %f %f %f" offs x y z radio
                        CREATE_CHECKPOINT 1 x y z x y z radio checkp
                        ADD_BLIP_FOR_COORD x y z blip
                        CHANGE_BLIP_COLOUR blip 1

                        GET_LABEL_POINTER Opponentslap offs
                        WRITE_MEMORY offs 4 -1 0
                        i=0
                    ELSE
                        GET_LABEL_POINTER Coords2 offs
                        WRITE_MEMORY offs 4 radio 0
                        SCAN_STRING $coords "%f %f %f %f" offs x2 y2 z2 radio
                        GET_LABEL_POINTER Coords2 offs
                        READ_MEMORY offs 4 0 radio
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                            CREATE_CHECKPOINT 3 x y z x2 y2 z2 radio checkp
                        ELSE                           
                            CREATE_CHECKPOINT 0 x y z x2 y2 z2 radio checkp
                        ENDIF
                        ADD_BLIP_FOR_COORD x y z blip
                        CHANGE_BLIP_COLOUR blip 0
                        i=0 
                    ENDIF
                ENDIF
            ELSE
                DELETE_CHECKPOINT checkp
                REMOVE_BLIP blip
                READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" selected
                    IF selected=0
                        SCAN_STRING $coords "%f %f %f %f" offs x y z radio
                        CREATE_CHECKPOINT 1 x y z x y z radio checkp
                        ADD_BLIP_FOR_COORD x y z blip
                        CHANGE_BLIP_COLOUR blip 0
                        GET_LABEL_POINTER Opponentslap offs
                        WRITE_MEMORY offs 4 -1 0
                        i=0
                    ELSE
                        GET_LABEL_POINTER Coords2 offs
                        WRITE_MEMORY offs 4 radio 0
                        SCAN_STRING $coords "%f %f %f %f" offs x2 y2 z2 radio
                        GET_LABEL_POINTER Coords2 offs
                        READ_MEMORY offs 4 0 radio
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                            CREATE_CHECKPOINT 3 x y z x2 y2 z2 radio checkp
                        ELSE                           
                            CREATE_CHECKPOINT 0 x y z x2 y2 z2 radio checkp
                        ENDIF
                        ADD_BLIP_FOR_COORD x y z blip
                        CHANGE_BLIP_COLOUR blip 0
                        i=0 
                    ENDIF
            ENDIF
        ENDIF
    IF aux=2
        radio+=100.0
    ENDIF
    CLEO_RETURN 0 checkp blip x y z radio i isTrue 

   

}
{
    LVAR_INT m s ms m2 s2 ms2 name coords offs scplayer car id bool count
    LVAR_TEXT_LABEL16 idd min seg milseg
    updateRecord:

        IF m<10
            STRING_FORMAT min "0%i" m
        ELSE
            STRING_FORMAT min "%i" m
        ENDIF

        IF s<10
            STRING_FORMAT seg "0%i" s
        ELSE
            STRING_FORMAT seg "%i" s
        ENDIF

        IF ms<10
            STRING_FORMAT milseg "00%i" ms
        ELSE
            IF ms<100
                STRING_FORMAT milseg "0%i" ms
            ELSE
                STRING_FORMAT milseg "%i" ms
            ENDIF
        ENDIF


        STRING_FORMAT idd "%i:%i.%i" m s ms
        SCAN_STRING $idd "%i:%i.%i" count m s ms

        GET_PLAYER_CHAR 0 scplayer
        GET_CAR_CHAR_IS_USING scplayer car
        GET_CAR_MODEL car id

        GET_LABEL_POINTER RaceName coords
        GET_LABEL_POINTER Coords name

        STRING_FORMAT name "CLEO/Race creator++/Times/%s" coords

        IF READ_STRING_FROM_INI_FILE $name "Global" "BestTime" idd
            SCAN_STRING $idd "%i:%i.%i" count m2 s2 ms2
            CLEO_CALL isTimeLower 0 m2 s2 ms2 m s ms bool

            IF bool=1
                STRING_FORMAT idd "%s:%s.%s" $min $seg $milseg
                WRITE_STRING_TO_INI_FILE $idd $name "Global" "BestTime"
            ENDIF

            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" id

            IF READ_STRING_FROM_INI_FILE $name $offs "BestTime" idd
                SCAN_STRING $idd "%i:%i.%i" count m2 s2 ms2
                CLEO_CALL isTimeLower 0 m2 s2 ms2 m s ms bool

                IF bool=1
                    STRING_FORMAT idd "%s:%s.%s" $min $seg $milseg
                    WRITE_STRING_TO_INI_FILE $idd $name $offs "BestTime"
                ENDIF
            ELSE
                STRING_FORMAT idd "%s:%s.%s" $min $seg $milseg
                WRITE_STRING_TO_INI_FILE $idd $name $offs "BestTime"
            ENDIF



        ELSE
            
            STRING_FORMAT idd "%s:%s.%s" $min $seg $milseg
            WRITE_STRING_TO_INI_FILE $idd $name "Global" "BestTime"

            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" id

            IF NOT READ_STRING_FROM_INI_FILE $name $offs "BestTime" idd
                STRING_FORMAT idd "%s:%s.%s" $min $seg $milseg
                WRITE_STRING_TO_INI_FILE $idd $name $offs "BestTime"
            ENDIF
        ENDIF
        CLEO_RETURN 0
}
{
    LVAR_INT m2 s2 ms2 m s ms
    isTimeLower:
        IF m<m2
            CLEO_RETURN 0 1
        ELSE
            IF m>m2
                CLEO_RETURN 0 0
            ELSE
                IF s<s2
                    CLEO_RETURN 0 1
                ELSE
                    IF s>s2
                        CLEO_RETURN 0 0
                    ELSE
                        IF ms<ms2
                            CLEO_RETURN 0 1
                        ELSE
                            CLEO_RETURN 0 0
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
}
{
LVAR_INT g coords selected offs almacen ich char testcar scplayer filename type blip numero
LVAR_FLOAT x2 y2 z2 radio angle traccion roll
startgrid:
    almacen=1
    IF almacen=9
        GET_PLAYER_CHAR 0 scplayer
        GET_CAR_CHAR_IS_USING scplayer testcar
    ENDIF
    GET_LABEL_POINTER Dumper filename
    
    GET_LABEL_POINTER Helper coords
    coords+=4
    READ_MEMORY coords 4 0 numero

    WHILE almacen < numero
        WAIT 0
        
        GET_LABEL_POINTER Opponents coords
        offs=almacen*4
        coords+=offs
        READ_MEMORY coords 4 0 testcar
        GET_LABEL_POINTER Coords coords

        READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
        SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" offs x2 y2 z2 radio ich angle type traccion roll


        SET_EXTENDED_CAR_VAR testcar "RaceC++" 10 1
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 11 1

        SET_EXTENDED_CAR_VAR testcar "RaceC++" 5 x2
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 6 y2
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 7 z2
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 8 ich
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 13 roll
        
        READ_STRING_FROM_INI_FILE $filename "Checkpoints" "1" coords
        SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" offs x2 y2 z2 radio ich angle type traccion roll

        SET_EXTENDED_CAR_VAR testcar "RaceC++" 1 x2
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 2 y2
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 3 z2
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 4 radio
        SET_EXTENDED_CAR_VAR testcar "RaceC++" 12 type

        GET_LABEL_POINTER RaceOpts coords
        coords+=16
        READ_MEMORY coords 4 0 offs
        IF offs=0
            SET_CAR_CAN_BE_VISIBLY_DAMAGED testcar 0
            SET_CAR_CAN_BE_VISIBLY_DAMAGED testcar 0
        ENDIF

        FREEZE_CAR_POSITION testcar 1
        FREEZE_CAR_POSITION testcar 0
        SET_CAR_AS_MISSION_CAR testcar
        SET_CAR_MISSION testcar 33
        GET_DRIVER_OF_CAR testcar char
        CLEAR_CHAR_TASKS char
        TASK_CAR_MISSION char testcar testcar 33 100.0 2
        CLEAR_CHAR_TASKS char
        IF IS_WANTED_LEVEL_GREATER 0 0
            SET_CHAR_WANTED_BY_POLICE char 1
        ENDIF
        READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "MarkerForOpponents" offs
        READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "ShowOpponentsMap" coords
        IF offs=1
            IF coords=0
                ADD_BLIP_FOR_CAR testcar selected
                CHANGE_BLIP_COLOUR selected 0
                CHANGE_BLIP_DISPLAY selected 1
            ELSE
                ADD_BLIP_FOR_CAR testcar selected
                CHANGE_BLIP_COLOUR selected 2
                CHANGE_BLIP_SCALE selected 1
            ENDIF
        ELSE
            IF coords=1
                ADD_BLIP_FOR_CAR testcar selected
                CHANGE_BLIP_COLOUR selected 2
                CHANGE_BLIP_SCALE selected 1
                CHANGE_BLIP_DISPLAY selected 2
            ENDIF
        ENDIF
        GET_VEHICLE_SUBCLASS testcar selected
        IF selected=VEHICLE_SUBCLASS_HELI
            HELI_GOTO_COORDS testcar x2 y2 z2 0.0 z2
        ELSE
            GET_CAR_MODEL testcar offs
            IF selected=VEHICLE_SUBCLASS_PLANE
            //AND NOT offs=539
                SET_CAR_FORWARD_SPEED testcar 20.0
                PLANE_GOTO_COORDS testcar x2 y2 z2 0.0 z2
                CLEAR_CHAR_TASKS char
                SET_CAR_CRUISE_SPEED testcar angle
                PLANE_GOTO_COORDS testcar x2 y2 z2 0.0 z2
            ELSE
                IF selected=VEHICLE_SUBCLASS_BOAT
                    BOAT_GOTO_COORDS testcar x2 y2 z2
                    SET_BOAT_CRUISE_SPEED testcar angle
                    SET_CAR_FORWARD_SPEED testcar 20.0
                    CLEAR_CHAR_TASKS char
                    SET_CAR_CRUISE_SPEED testcar angle
                    SET_BOAT_CRUISE_SPEED testcar angle
                    BOAT_GOTO_COORDS testcar x2 y2 z2
                    
                ELSE
                    IF ich=0
                        GET_DRIVER_OF_CAR testcar char
                        CLEAR_CHAR_TASKS char
                        CAR_GOTO_COORDINATES_RACING testcar x2 y2 z2
                        SET_CAR_DRIVING_STYLE testcar 2
                        SET_CAR_CRUISE_SPEED testcar angle
                    ELSE
                        //CAR_GOTO_COORDINATES testcar x2 y2 z2
                        GET_DRIVER_OF_CAR testcar char
                        CLEAR_CHAR_TASKS char
                        TASK_CAR_DRIVE_TO_COORD char testcar x2 y2 z2 angle 2 0 2
                        SET_CAR_CRUISE_SPEED testcar angle
                        //SET_CAR_DRIVING_STYLE testcar 2
                        //SET_CAR_STRAIGHT_LINE_DISTANCE testcar 250            
                    ENDIF
                ENDIF
            ENDIF

        ENDIF
        // GET_CAR_MODEL testcar offs
        // IF offs=539
        //     GET_DRIVER_OF_CAR testcar char
        //     TASK_CAR_DRIVE_TO_COORD char testcar x2 y2 z2 angle 2 0 2
        // ENDIF
        
        SET_CAR_CAN_GO_AGAINST_TRAFFIC testcar 1
        //SET_LOAD_COLLISION_FOR_CHAR_FLAG char 0
        SET_LOAD_COLLISION_FOR_CAR_FLAG testcar 0
        SET_CAR_HEALTH testcar 50000
        offs=0
        // GET_LABEL_POINTER Opponentsch offs
        // selected=almacen*4
        // offs+=selected
        // WRITE_MEMORY offs 4 1 0
        almacen+=1
    ENDWHILE
    CLEO_RETURN 0
}
{
    LVAR_INT scplayer filename almacen offs selected coords recordT showTime showCurrent car id name timer lap
    LVAR_TEXT_LABEL16 record grecord drecord

    showTTtimers:
        STREAM_CUSTOM_SCRIPT_FROM_LABEL ttTimers
        CLEO_RETURN 0
    

    ttTimers:

        SET_TEXT_DRAW_BEFORE_FADE 1

        GET_LABEL_POINTER Dumper filename
        GET_PLAYER_CHAR 0 scplayer
        GET_CAR_CHAR_IS_USING scplayer car
        GET_CAR_MODEL car id

        READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "GlobalBestTime" recordT
        READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "ShowBestTime" showTime
        READ_INT_FROM_INI_FILE "Cleo/Race creator++/General settings.ini" "Settings" "ResetTimer" showCurrent

        IF showTime=0
        AND showCurrent=0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF


        GET_LABEL_POINTER RaceName coords
        GET_LABEL_POINTER Coords name

        STRING_FORMAT name "CLEO/Race creator++/Times/%s" coords
        READ_STRING_FROM_INI_FILE $name "Global" "BestTime" grecord
        GET_LABEL_POINTER Istring offs
        STRING_FORMAT offs "%i" id
        READ_STRING_FROM_INI_FILE $name $offs "BestTime" record

        IF recordT=0
            STRING_FORMAT drecord "~g~%s" $record         
        ELSE
            STRING_FORMAT drecord "~g~%s" $grecord
        ENDIF

        GET_LABEL_POINTER Opponentslap selected
        READ_MEMORY selected 4 0 lap

        WHILE TRUE
            WAIT 0

            IF showTime=1
                DRAW_STRING_EXT "Best time:" DRAW_EVENT_BEFORE_HUD 544.0 360.0 0.45 1.1 0 2 1 0 220.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                DRAW_STRING_EXT $drecord DRAW_EVENT_BEFORE_HUD 544.0 380.0 0.45 1.1 0 2 1 0 220.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            ENDIF

            GET_LABEL_POINTER Opponentslap selected
            READ_MEMORY selected 4 0 almacen

            IF NOT almacen=lap
                GET_LABEL_POINTER RaceName coords
                GET_LABEL_POINTER Coords name

                STRING_FORMAT name "CLEO/Race creator++/Times/%s" coords
                READ_STRING_FROM_INI_FILE $name "Global" "BestTime" grecord
                GET_LABEL_POINTER Istring offs
                STRING_FORMAT offs "%i" id
                READ_STRING_FROM_INI_FILE $name $offs "BestTime" record

                IF recordT=0
                    STRING_FORMAT drecord "~g~%s" $record         
                ELSE
                    STRING_FORMAT drecord "~g~%s" $grecord
                ENDIF

                lap=almacen    
            ENDIF

            GET_LABEL_POINTER Helper coords
            READ_MEMORY coords 4 0 offs
            IF offs=5
                SET_TEXT_DRAW_BEFORE_FADE 0
                FREEZE_ONSCREEN_TIMER 1
                TERMINATE_THIS_CUSTOM_SCRIPT
            ENDIF
        ENDWHILE
}
{
    LVAR_INT isnew scplayer filename almacen offs coords isTrue filen g i selected car checkp blip listaObjetos testcar listaPos tipo
    LVAR_FLOAT x y z radio angle traccion w
    LVAR_TEXT_LABEL16 idd
    car=CAR
    createPreps:
        GET_PLAYER_CHAR 0 scplayer
        WAIT 0
        SET_EVERYONE_IGNORE_PLAYER 0 1
        DISABLE_ALL_ENTRY_EXITS 1
        SET_POLICE_IGNORE_PLAYER 0 1
        SET_CHAR_PROOFS scplayer 1 1 1 1 1
        DISPLAY_ZONE_NAMES 0
        DO_FADE 100 0
        WAIT 200
        GET_LABEL_POINTER Checkpoint offs
        offs+=16
        WRITE_MEMORY offs 4 -2 0
        SET_CAR_DENSITY_MULTIPLIER 0.0
        SET_PED_DENSITY_MULTIPLIER 0.0
        GET_CHAR_COORDINATES scplayer x y z
        CLEAR_AREA x y z 2000.0 1
        GET_LABEL_POINTER Aioptions almacen
        WRITE_MEMORY almacen 4 0 0
        almacen+=4
        WRITE_MEMORY almacen 4 100 0 
        almacen+=4
        WRITE_MEMORY almacen 4 1 0 
        CREATE_LIST DATATYPE_INT listaPos
        CREATE_LIST DATATYPE_INT listaObjetos
        DO_FADE 100 1
        WAIT 200
        GET_LABEL_POINTER Dumper filename
        isTrue=1
        IF isnew=1  //si es nueva, crea un archivo .ini
            GOSUB setfilename
            GET_LABEL_POINTER PlayerInt offs
            WRITE_MEMORY offs 4 0 0
            GET_LABEL_POINTER Coords coords
            FIND_FIRST_FILE $filename g coords
            GET_LABEL_POINTER RaceName filen
            STRING_FORMAT filen "%s" coords
            FIND_CLOSE g
            i=-1 //checkpoint conter
            g=-1 //opponents conter
            CLEO_CALL createrace 0 filename listaObjetos g i listaPos 0
        ELSE
            i=0
            GET_LABEL_POINTER Coords coords
            FIND_FIRST_FILE $filename g coords
            GET_LABEL_POINTER RaceName filen
            STRING_FORMAT filen "%s" coords
            FIND_CLOSE g
            GET_LABEL_POINTER PlayerCoords coords
            GET_CHAR_COORDINATES scplayer x y z
            STRING_FORMAT coords "%f %f %f" x y z 
            GET_LABEL_POINTER PlayerInt offs
            GET_CHAR_AREA_VISIBLE scplayer selected
            WRITE_MEMORY offs 4 selected 0
            offs+=4
            WRITE_MEMORY offs 4 -2 0
            WHILE isTrue=1
                WAIT 0
                GET_LABEL_POINTER Istring offs
                STRING_FORMAT offs "%i" i
                GET_LABEL_POINTER Coords coords
                IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
                    IF IS_STRING_EQUAL $coords "DELETED" 7 0 "p"
                        GOSUB true0
                    ELSE
                        IF NOT SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" selected x y z radio filen angle tipo traccion w
                            GOSUB updateCheck
                            WRITE_FLOAT_TO_INI_FILE 2.0 $filename "Settings" "ModVersion"
                        ENDIF
                        i+=1
                    ENDIF
                ELSE
                    GOSUB true0
                ENDIF       
            ENDWHILE

            IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
            AND NOT IS_STRING_EQUAL $coords "DELETED" 7 0 "p"
                READ_INT_FROM_INI_FILE $filename "Settings" "InteriorID" offs
                SET_AREA_VISIBLE offs
                SET_CHAR_AREA_VISIBLE scplayer offs
                IF IS_CHAR_IN_ANY_CAR scplayer
                    GET_CAR_CHAR_IS_USING scplayer car
                    SET_VEHICLE_AREA_VISIBLE car offs
                ENDIF

                
                SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" offs x y z radio filen angle tipo traccion w

                SET_CHAR_COORDINATES scplayer x y z
            ELSE
                WRITE_INT_TO_INI_FILE 0 $filename "Settings" "InteriorID"
            ENDIF
            
            isTrue=1
            g=0
            WHILE isTrue=1
                WAIT 0
                GET_LABEL_POINTER Istring offs
                STRING_FORMAT offs "%i" g
                GET_LABEL_POINTER Coords coords
                IF READ_STRING_FROM_INI_FILE $filename "Vehicle Positions" $offs coords
                    IF IS_STRING_EQUAL $coords "DELETED" 7 0 "p"
                        // IF g=0
                        //     g=-1
                        // ENDIF 
                        g-=1      
                        GOSUB true0v
                        isTrue=0
                    ELSE
                        g+=1
                    ENDIF
                ELSE
                    g-=1
                    GOSUB true0v
                    isTrue=0
                ENDIF
            ENDWHILE
            
            CLEO_CALL loadObjects 0 listaObjetos filename 0
            CLEO_CALL createrace 0 filename listaObjetos g i listaPos 0
        ENDIF
        DO_FADE 100 1
        WAIT 200
        CLEO_RETURN 0
    true0:
        i-=1
        IF i>-1
            GET_LABEL_POINTER Coords coords
            READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
            SCAN_STRING $coords "%f %f %f %f" offs x y z radio
            CREATE_CHECKPOINT 1 x y z x y z radio checkp
            ADD_BLIP_FOR_COORD x y z blip
            CHANGE_BLIP_COLOUR blip 1
            GET_LABEL_POINTER Checkpoint offs
            WRITE_MEMORY offs 4 checkp 0
            offs+=4
            WRITE_MEMORY offs 4 blip 0
        ENDIF
        IF i>1
            i-=1
            GET_LABEL_POINTER Coords coords
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" i
            READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
            SCAN_STRING $coords "%f %f %f %f" offs x y z radio
            CREATE_CHECKPOINT 0 x y z x y z radio checkp
            ADD_BLIP_FOR_COORD x y z blip
            CHANGE_BLIP_COLOUR blip 0
            GET_LABEL_POINTER Checkpoint offs
            offs+=8
            WRITE_MEMORY offs 4 checkp 0
            offs+=4
            WRITE_MEMORY offs 4 blip 0
            i+=1
        ENDIF
        IF i>0
            GET_LABEL_POINTER Coords coords
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" i
            READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
            SCAN_STRING $coords "%f %f %f %f" offs x y z radio
            CREATE_CHECKPOINT 0 x y z x y z radio checkp
            ADD_BLIP_FOR_COORD x y z blip
            CHANGE_BLIP_COLOUR blip 0
            GET_LABEL_POINTER Checkpoint offs
            offs+=16
            WRITE_MEMORY offs 4 checkp 0
            offs+=4
            WRITE_MEMORY offs 4 blip 0
        ENDIF
        isTrue=0
        RETURN

    //
    true0v:
        
        almacen=0
        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
        IF IS_STRING_EQUAL $idd "STREET" 7 0 "m"
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestCarID" testcar 
        ENDIF
        IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestAirID" testcar
        ENDIF
        IF IS_STRING_EQUAL $idd "SEA" 7 0 "m"
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestSeaID" testcar
        ENDIF
        WHILE almacen<=g
            WAIT 0
            GET_LABEL_POINTER Coords coords
            GET_LABEL_POINTER Istring offs
            STRING_FORMAT offs "%i" almacen
            READ_STRING_FROM_INI_FILE $filename "Vehicle Positions" $offs coords
            SCAN_STRING $coords "%f %f %f %f %f" offs x y z radio angle
            REQUEST_MODEL testcar
            WHILE NOT HAS_MODEL_LOADED testcar
                WAIT 0
            ENDWHILE
            

            CREATE_CAR testcar x y z car
            MARK_MODEL_AS_NO_LONGER_NEEDED testcar
          
            SET_CAR_COORDINATES car x y z
            SET_CAR_HEADING car angle
            SET_CAR_PROOFS car 1 1 1 1 1
            FREEZE_CAR_POSITION_AND_DONT_LOAD_COLLISION car 1
            SET_CAR_COLLISION car 0
            

            CHANGE_CAR_COLOUR car 2 2
            IF almacen=0
                CHANGE_CAR_COLOUR car 5 5
            ENDIF
            LIST_ADD listaPos car
            almacen+=1
        ENDWHILE
        RETURN
    updateCheck:
        IF NOT SCAN_STRING $coords "%f %f %f %f %i %f %i" selected x y z radio filen angle tipo
            STRING_FORMAT coords "%f %f %f %f %i %f %i" x y z radio filen angle 1
            WRITE_STRING_TO_INI_FILE $coords $filename "Checkpoints" $offs
        ENDIF
        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
        SCAN_STRING $coords "%f %f %f %f %i %f %i" selected x y z radio filen angle tipo
        STRING_FORMAT coords "%f %f %f %f %i %f %i %.2f %.1f" x y z radio filen angle tipo 1.0 0.0
        WRITE_STRING_TO_INI_FILE $coords $filename "Checkpoints" $offs
        RETURN 

    
    //set file.ini name and options
    setfilename:
        WAIT 0
        IF FIND_FIRST_FILE "Cleo/Race Creator++/Races/*.ini" i idd
            filen=1 
            WHILE isTrue=1
            WAIT 0
                IF FIND_NEXT_FILE i idd
                    filen=filen+1
                ELSE
                    filen=filen+1
                    GET_LABEL_POINTER Dumper filename
                    STRING_FORMAT filename "Cleo/Race Creator++/Races/Race%i.ini" filen
                    IF NOT DOES_FILE_EXIST $filename
                        GET_CHAR_AREA_VISIBLE scplayer offs
                        WRITE_INT_TO_INI_FILE offs $filename "Settings" "InteriorID"
                        WRITE_INT_TO_INI_FILE 0 $filename "Settings" "IsCircuit"
                        GET_LABEL_POINTER Istring offs
                        STRING_FORMAT offs "STREET"
                        WRITE_STRING_TO_INI_FILE $offs $filename "Settings" "Race type"
                        WRITE_INT_TO_INI_FILE 0 $filename "Settings" "Reward"
                        isTrue=0
                    ENDIF
                ENDIF
            ENDWHILE
        ELSE
            GET_LABEL_POINTER Dumper filename
            STRING_FORMAT filename "Cleo/Race Creator++/Races/Race1.ini" filen
            IF NOT DOES_FILE_EXIST $filename
                GET_CHAR_AREA_VISIBLE scplayer offs
                WRITE_INT_TO_INI_FILE offs $filename "Settings" "InteriorID"
                WRITE_INT_TO_INI_FILE 0 $filename "Settings" "IsCircuit"
                GET_LABEL_POINTER Istring offs
                STRING_FORMAT offs "STREET"
                WRITE_STRING_TO_INI_FILE $offs $filename "Settings" "Race type"
                WRITE_INT_TO_INI_FILE 0 $filename "Settings" "Reward"
            ENDIF    
        ENDIF

        PRINT_FORMATTED_NOW "~b~%s ~w~created" 2000 filename
        RETURN

}
{
    LVAR_INT filename listaObjetos g i listaPos checkp blip menu offs name selected coords isTrue almacen isnew char filen car testcar scplayer
    LVAR_FLOAT radio x y z angle w traccion vol
    LVAR_TEXT_LABEL16 idd
    //create race menu
    createrace:
        WAIT 0
        name=0
        GET_PLAYER_CHAR 0 scplayer
        SET_PLAYER_CONTROL 0 0
        GOSUB checkplane
        GET_LABEL_POINTER Checkpoint offs
        offs+=16
        READ_MEMORY offs 4 0 selected
        IF NOT selected=-2 //si no es nueva, recupera el ultimo checkpoint
            READ_MEMORY offs 4 0 checkp
            WRITE_MEMORY offs 4 -2 0
            offs+=4
            READ_MEMORY offs 4 0 blip
        ENDIF

        SET_TEXT_DRAW_BEFORE_FADE 1
        USE_TEXT_COMMANDS 1
        
        GET_LABEL_POINTER RaceName name
        GET_LABEL_POINTER Coords coords
        GET_STRING_LENGTH $name selected
        w=# selected
        w*=6.0
        LOAD_TEXTURE_DICTIONARY RCPLUS
        menu=0
        IF READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
            IF IS_STRING_EQUAL $idd "STREET" 10 0 "z"
                LOAD_SPRITE 2 "car_logo"
            ENDIF
            IF IS_STRING_EQUAL $idd "AIR" 10 0 "z"
                LOAD_SPRITE 2 "plane_logo"
            ENDIF
            IF IS_STRING_EQUAL $idd "SEA" 10 0 "z"
                LOAD_SPRITE 2 "boat_logo"
            ENDIF
        ELSE
            LOAD_SPRITE 2 "car_logo"
        ENDIF

        READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
        IF offs=1
            LOAD_SPRITE 1 "circuit_logo_s"
        ELSE
            LOAD_SPRITE 1 "sprint_logo_s"
        ENDIF

        LOAD_SPRITE 3 "create_bg"
        //SET_SPRITES_DRAW_BEFORE_FADE 1
        WHILE TRUE
            WAIT 0

            //help
            DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 380.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~WASD - Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 399.7 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 415.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 430.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200

            //

            GOSUB showRaceInfo
            
            GET_TEXTURE_FROM_SPRITE 3 coords
            DRAW_TEXTURE_PLUS coords DRAW_EVENT_BEFORE_HUD 80.0 215.0 120.0 200.0 180.0 0.0 0 0 0 255 255 255 200
            IF IS_KEY_JUST_PRESSED VK_DOWN
            OR IS_KEY_JUST_PRESSED VK_KEY_S
                IF menu>3
                    menu=0
                ELSE
                    menu+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_UP
            OR IS_KEY_JUST_PRESSED VK_KEY_W
                IF menu<1
                    menu=4
                ELSE
                    menu-=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF
            
            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 120.0 120.0 30.0 180.0 0.0 0 0 0 120 120 120 220//140 80 10 220 //20 20 20 255
            DRAW_STRING_EXT "The editor" DRAW_EVENT_BEFORE_HUD 80.0 110.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 255 79 28 200
            DRAW_RECT 80.0 105.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 135.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 315.0 120.0 3.0 50 50 50 255
            SWITCH menu
                CASE 0
                    DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Racers" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Objects" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Options" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 280.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Racers" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90  200
                    DRAW_STRING_EXT "Objects" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Options" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 280.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 2
                    DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Racers" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Objects" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Options" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 280.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 3
                    DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Racers" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Objects" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Options" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90  200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 280.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 4
                    DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Racers" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Objects" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Options" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 280.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90  200
                    BREAK
            ENDSWITCH

            IF IS_KEY_JUST_PRESSED VK_EXECUTE
            OR IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                WAIT 0
                REMOVE_TEXTURE_DICTIONARY
                GOSUB stopc
                GOSUB checkplane2
                CLEO_RETURN 0
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_SPACE
                SWITCH menu
                    CASE 0
                        GOTO checkpointmenu
                        BREAK
                    CASE 1
                        GOTO oppmenu
                        BREAK
                    CASE 2
                        CLEO_CALL menuobjeto 0 listaObjetos filename 0
                        SET_CAMERA_BEHIND_PLAYER
                        GOTO createrace
                        BREAK
                    CASE 3
                        GOTO opsmenu
                        BREAK                                        
                    CASE 4
                        GOSUB stopc
                        GOSUB checkplane2
                        CLEO_RETURN 0
                        BREAK
                ENDSWITCH
            ENDIF
        ENDWHILE
        CLEO_RETURN 0
        
    //checkpoints menu
    checkpointmenu:
        SET_PLAYER_CONTROL 0 0
        GOSUB checkplane
        WAIT 200
        menu=0
        LOAD_SPRITE 3 "checkp_bg"

        WHILE TRUE
            WAIT 0
            GOSUB showRaceInfo
            
            GET_TEXTURE_FROM_SPRITE 3 coords
            
            DRAW_TEXTURE_PLUS coords DRAW_EVENT_BEFORE_HUD 80.0 195.0 120.0 180.0 180.0 0.0 0 0 0 255 255 255 160
            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 120.0 120.0 30.0 180.0 0.0 0 0 0 120 120 120 220//140 80 10 220

            DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 110.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 255 79 28 200
            
            DRAW_RECT 80.0 105.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 135.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 285.0 120.0 3.0 50 50 50 255

            SWITCH menu
                CASE 0
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 2
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 3
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 220.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 250.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 160 30 30 200
                    BREAK
            ENDSWITCH
            
            IF IS_KEY_JUST_PRESSED VK_DOWN
            OR IS_KEY_JUST_PRESSED VK_KEY_S
                IF menu>2
                    menu=0
                ELSE
                    menu+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_UP
            OR IS_KEY_JUST_PRESSED VK_KEY_W
                IF menu<1
                    menu=3
                ELSE
                    menu-=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_EXECUTE
            OR IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                GOTO createrace
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_SPACE
                SWITCH menu
                    CASE 0             
                        radio=10.0
                        GET_LABEL_POINTER Aioptions almacen
                        almacen+=4
                        WRITE_MEMORY almacen 4 100 0

                        GOTO addcheckpoint
                        BREAK
                    CASE 2               
                        GOTO delcheckpoint
                        BREAK
                    CASE 1    
                        GET_LABEL_POINTER Coords coords
                        IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" "0" coords
                        AND NOT IS_STRING_EQUAL $coords "DELETED" 7 0 "m"
                            almacen=1
                            GET_LABEL_POINTER Auxiliar offs
                            WRITE_MEMORY offs 4 -1 0
                            offs+=4
                            WRITE_MEMORY offs 4 -1 0
                            isnew=1
                            GOTO editcheckpoint
                        ELSE
                            GET_AUDIO_SFX_VOLUME angle
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                            PRINT_FORMATTED_NOW "There are not checkpoints!" 1000
                        ENDIF
                        BREAK
                    CASE 3
                        GOTO createrace
                        BREAK
                ENDSWITCH
            ENDIF
        ENDWHILE

    //add checkpoint menu
    addcheckpoint:
        SET_CHAR_PROOFS scplayer 1 1 1 1 1
        SET_PLAYER_ENTER_CAR_BUTTON 0 0
        SET_PLAYER_CONTROL 0 1
        traccion=1.0

        GOSUB checkplane2
        WAIT 300
        
        GET_LABEL_POINTER Aioptions almacen
        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
        IF NOT IS_STRING_EQUAL $idd "STREET" 7 0 "z"
            WRITE_MEMORY almacen 4 1 0
        ENDIF
        READ_MEMORY almacen 4 0 coords
        IF coords=0
            idd="~g~Default"
        ELSE
            idd="~g~Direct"
        ENDIF

        ADD_TEXT_LABEL RMFFME0 $idd

        almacen+=4
        READ_MEMORY almacen 4 0 selected //lee la velocidad de la ia
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "~g~%i" selected
        ADD_TEXT_LABEL RMFFME1 $coords

        almacen+=4
        READ_MEMORY almacen 4 0 offs
        IF offs=0
            STRING_FORMAT coords "~g~Guiding"
        ELSE
            IF offs=1
                STRING_FORMAT coords "~g~Default"
            ELSE
                STRING_FORMAT coords "~g~Teleporter"
            ENDIF
        ENDIF
        ADD_TEXT_LABEL RMFFME2 $coords

        GET_LABEL_POINTER Coords coords
        offs=#radio
        STRING_FORMAT coords "~g~%i" offs
        ADD_TEXT_LABEL RMFFME3 $coords

        GET_LABEL_POINTER Coords coords

        almacen=0
        WHILE TRUE
            WAIT 0
            GOSUB testmenu
            GOSUB showRaceInfo
            
            DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 340.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~NUM8-2 / Up-Down Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 360.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~NUM4-6 / Left-Right Arrows~w~: Change value" DRAW_EVENT_BEFORE_HUD 555.0 375.3 0.14 0.5 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~NUM5 / Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 389.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~Keep H~w~: Show help" DRAW_EVENT_BEFORE_HUD 555.0 404.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200
            DRAW_STRING_EXT "~y~Z~w~: Hide menu" DRAW_EVENT_BEFORE_HUD 555.0 419.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 434.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200

            IF IS_KEY_PRESSED VK_KEY_B
                DRAW_STRING_EXT "~y~SHIFT+L~w~: Spawn test car" DRAW_EVENT_AFTER_DRAWING 555.0 230.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                DRAW_STRING_EXT "~y~+/-~w~: Change radius" DRAW_EVENT_AFTER_DRAWING 555.0 245.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                DRAW_STRING_EXT "~y~K~w~: Change behavior" DRAW_EVENT_AFTER_DRAWING 555.0 260.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                DRAW_STRING_EXT "~y~I~w~: Change type" DRAW_EVENT_AFTER_DRAWING 555.0 275.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                DRAW_STRING_EXT "~y~/~w~: Change velocity" DRAW_EVENT_AFTER_DRAWING 555.0 290.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                DRAW_STRING_EXT "~y~O/P~w~: Change traction" DRAW_EVENT_AFTER_DRAWING 555.0 305.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                DRAW_STRING_EXT "~y~X~w~: Auto-creation checkpoints" DRAW_EVENT_AFTER_DRAWING 555.0 320.0 0.15 0.6 0 2 1 0 200.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            ELSE
                DRAW_STRING_EXT "Keep B to see more controls:" DRAW_EVENT_AFTER_DRAWING 555.0 320.0 0.15 0.6 0 2 1 0 170.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            ENDIF

            GOSUB testmenu //testcar

            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 215.0 120.0 215.0 180.0 0.0 0 0 0 0 0 0 200 //coso grande
            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 120.0 120.0 30.0 180.0 0.0 0 0 0 120 120 120 220
            DRAW_STRING_EXT "Checkpoints" DRAW_EVENT_BEFORE_HUD 80.0 110.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 255 79 28 200
            
            DRAW_RECT 80.0 105.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 135.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 320.0 120.0 3.0 50 50 50 255
            
            SWITCH almacen
                CASE 0
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 10 90 10 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200                    
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 40 50 40 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 197.0 60.0 15.0 0.0 0.0 0 0 0 0 50 0 200
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "Its the checkpoint size. Default is 10." 100
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_LEFT
                    OR IS_KEY_JUST_PRESSED VK_NUMPAD4
                        GOSUB radioLow
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_RIGHT
                    OR IS_KEY_JUST_PRESSED VK_NUMPAD6
                        GOSUB radioAdd
                    ENDIF
                    BREAK
                CASE 2
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 40 50 40 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 217.0 115.0 15.0 0.0 0.0 0 0 0 0 50 0 200
                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 60.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "If its ~g~Default~w~ then opponents they will follow paths. If~g~Direct~w~ they will go directly to the checkpoint." 100
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_LEFT
                    OR IS_KEY_JUST_PRESSED VK_RIGHT
                    OR IS_KEY_JUST_PRESSED VK_NUMPAD4
                    OR IS_KEY_JUST_PRESSED VK_NUMPAD6
                        GOSUB behChange
                    ENDIF
                    BREAK
                CASE 3
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 40 50 40 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 237.0 90.0 15.0 0.0 0.0 0 0 0 0 50 0 200
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "Opponents ~b~max speed~w~ to reach this checkpoint." 100
                    ENDIF

                    IF IS_KEY_PRESSED VK_LEFT
                    OR IS_KEY_PRESSED VK_NUMPAD4
                        GOSUB speedLow
                    ENDIF
                        
                    IF IS_KEY_PRESSED VK_RIGHT
                    OR IS_KEY_PRESSED VK_NUMPAD6
                        GOSUB speedAdd
                    ENDIF
                    BREAK
                CASE 4
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 40 50 40 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.5 257.0 90.0 15.0 0.0 0.0 0 0 0 0 50 0 200
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 37.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "~g~Guiding~w~:Only the opponents will see this checkpoint. ~g~Teleporter~w~:Teleports to the next checkpoint." 100
                    ENDIF
                    IF IS_KEY_JUST_PRESSED VK_LEFT
                    OR IS_KEY_JUST_PRESSED VK_RIGHT
                    OR IS_KEY_JUST_PRESSED VK_NUMPAD4
                    OR IS_KEY_JUST_PRESSED VK_NUMPAD6
                        GOSUB changeVis
                    ENDIF
                    BREAK
                CASE 5
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 40 50 40 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.5 277.0 80.0 15.0 0.0 0.0 0 0 0 0 50 0 200
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "The ~b~traction control~w~ is how well the opponents will handle the car to reach this checkpoint. Default value is 1.0" 100
                    ENDIF

                    IF IS_KEY_PRESSED VK_LEFT
                    OR IS_KEY_PRESSED VK_NUMPAD4
                        GOSUB tractionLow
                    ENDIF
                        
                    IF IS_KEY_PRESSED VK_RIGHT
                    OR IS_KEY_PRESSED VK_NUMPAD6
                        GOSUB tractionAdd
                    ENDIF
                    BREAK
                CASE 6
                    DRAW_STRING_EXT "Add checkpoint" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 40 50 40 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    STRING_FORMAT idd "~g~%.2f" traccion
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 70 70 200
                    BREAK
            ENDSWITCH

            IF IS_KEY_JUST_PRESSED VK_KEY_Z
                WAIT 0
                WHILE NOT IS_KEY_JUST_PRESSED VK_KEY_Z
                    WAIT 0
                    PRINT_FORMATTED_NOW "Menu was closed. Press ~g~Z~w~ to open it again." 100
                ENDWHILE
            ENDIF   
            
        
        //fkeys
        //radio
            IF IS_KEY_JUST_PRESSED VK_ADD
                GOSUB radioAdd
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_SUBTRACT
                GOSUB radioLow
            ENDIF
        //enter
            IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                //CLEAR_ONSCREEN_COUNTER_LOCAL i
                //CLEAR_HELP
                //DELETE_MENU menu
                REMOVE_TEXT_LABEL RMFFME0
                REMOVE_TEXT_LABEL RMFFME1
                REMOVE_TEXT_LABEL RMFFME2
                REMOVE_TEXT_LABEL RMFFME3
                REMOVE_TEXT_LABEL RMFFME4
                REMOVE_TEXT_LABEL RMFFME5
                REMOVE_TEXT_LABEL RMFFME6
                REMOVE_TEXT_LABEL RMFFME7
                REMOVE_TEXT_LABEL RMFFME8
                REMOVE_TEXT_LABEL RMFFME9
                REMOVE_TEXT_LABEL RMFFM-1
                REMOVE_TEXT_LABEL RMFFM-2
                REMOVE_TEXT_LABEL RMFC-C1
                GOTO checkpointmenu
            ENDIF


        //ai beh fast key
            IF IS_KEY_JUST_PRESSED VK_KEY_K
                GOSUB behChange
            ENDIF

        //visibility fast key
            IF IS_KEY_JUST_PRESSED VK_KEY_I
                GOSUB changeVis
            ENDIF

        //ai speed fast key
            IF IS_KEY_PRESSED VK_OEM_PERIOD
                GOSUB speedAdd
            ENDIF
            IF IS_KEY_PRESSED VK_OEM_COMMA
                GOSUB speedLow
            ENDIF
        //traction fast key
            IF IS_KEY_JUST_PRESSED VK_KEY_P
                GOSUB tractionAdd
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_KEY_O
                GOSUB tractionLow
            ENDIF
        //
        IF IS_KEY_JUST_PRESSED VK_DOWN
        OR IS_KEY_JUST_PRESSED VK_NUMPAD2
            IF almacen=6
                almacen=0
            ELSE
                almacen+=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_UP
        OR IS_KEY_JUST_PRESSED VK_NUMPAD8
            IF almacen=0
                almacen=6
            ELSE
                almacen-=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF
        //select
            IF IS_KEY_JUST_PRESSED VK_SPACE
            OR IS_KEY_JUST_PRESSED VK_NUMPAD5
                SWITCH almacen
                    CASE 0
                        //GET_LABEL_POINTER Aioptions coords
                        //coords+=4
                        //WRITE_MEMORY coords 4 selected 0
                        GOSUB crearcheck
                        almacen=0
                        GET_LABEL_POINTER Coords coords
                        BREAK
                    CASE 6
                        REMOVE_TEXT_LABEL RMFFME0
                        REMOVE_TEXT_LABEL RMFFME1
                        REMOVE_TEXT_LABEL RMFFME2
                        REMOVE_TEXT_LABEL RMFFME3
                        REMOVE_TEXT_LABEL RMFFME4
                        REMOVE_TEXT_LABEL RMFFME5
                        REMOVE_TEXT_LABEL RMFFME6
                        REMOVE_TEXT_LABEL RMFFME7
                        REMOVE_TEXT_LABEL RMFFME8
                        REMOVE_TEXT_LABEL RMFFME9
                        REMOVE_TEXT_LABEL RMFFM-1
                        REMOVE_TEXT_LABEL RMFFM-2
                        REMOVE_TEXT_LABEL RMFC-C1
                        GOTO checkpointmenu
                    BREAK
                ENDSWITCH
            ENDIF       
            IF IS_KEY_JUST_PRESSED VK_KEY_X
                GOSUB chauto
            ENDIF

        ENDWHILE
    //create checkpoint and blip
    crearcheck:
        WAIT 0
        i=i+1
        IF i=0
            almacen = 0
            GET_CHAR_COORDINATES scplayer x y z
            READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
            IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                CREATE_CHECKPOINT 6 x y z x y z radio checkp
            ELSE
                CREATE_CHECKPOINT 1 x y z x y z radio checkp
            ENDIF
            ADD_BLIP_FOR_COORD x y z blip
            REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_CHECKPOINT_RED
            CHANGE_BLIP_COLOUR blip 1
            GET_LABEL_POINTER Checkpoint almacen
            WRITE_MEMORY almacen 4 (checkp) FALSE
            almacen+=4
            WRITE_MEMORY almacen 4 (blip) FALSE
        ENDIF                    
        IF i>0
            IF NOT i=1
                DELETE_CHECKPOINT checkp
                REMOVE_BLIP blip
            ENDIF
            IF i>1
                IF i>2  
                    GET_LABEL_POINTER Checkpoint coords
                    coords+=8
                    READ_MEMORY coords 4 0 checkp
                    DELETE_CHECKPOINT checkp
                    coords+=4
                    READ_MEMORY coords 4 0 blip
                    REMOVE_BLIP blip
                ENDIF
                i-=1
                GET_LABEL_POINTER Istring offs
                STRING_FORMAT offs "%i" i
                GET_LABEL_POINTER Coords coords
                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
                SCAN_STRING $coords "%f %f %f %f" selected x y z angle
                READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                    CREATE_CHECKPOINT 4 x y z x y z angle checkp
                ELSE
                    CREATE_CHECKPOINT 0 x y z x y z angle checkp
                ENDIF
                ADD_BLIP_FOR_COORD x y z blip
                CHANGE_BLIP_COLOUR blip 0
                GET_LABEL_POINTER Checkpoint coords
                coords+=8
                WRITE_MEMORY coords 4 checkp 0
                coords+=4
                WRITE_MEMORY coords 4 blip 0
                i+=1
            ENDIF
            //ADD_BLIP_FOR_COORD x y z blip
            //ENDIF
            GET_CHAR_COORDINATES scplayer x y z
            READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
            IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                CREATE_CHECKPOINT 4 x y z x y z radio checkp
            ELSE
                CREATE_CHECKPOINT 0 x y z x y z radio checkp
            ENDIF
            ADD_BLIP_FOR_COORD x y z blip
            REPORT_MISSION_AUDIO_EVENT_AT_POSITION 0.0 0.0 0.0 SOUND_CHECKPOINT_RED
            CHANGE_BLIP_COLOUR blip 0
        ENDIF
        GET_LABEL_POINTER Coords coords
        GET_LABEL_POINTER Aioptions almacen
        READ_MEMORY almacen 4 0 offs
        almacen+=4
        READ_MEMORY almacen 4 0 selected
        angle=#selected
        almacen+=4

        READ_MEMORY almacen 4 0 char

        IF IS_CHAR_IN_ANY_CAR scplayer
            GET_CAR_CHAR_IS_USING scplayer car
            GET_CAR_ROLL car vol
        ELSE
            vol=0.0
        ENDIF
        STRING_FORMAT coords "%f %f %f %f %i %f %i %.2f %f" x y z radio offs angle char traccion vol
        GET_LABEL_POINTER Istring selected
        STRING_FORMAT selected "%i" i
        WRITE_STRING_TO_INI_FILE $coords $filename "Checkpoints" $selected
        WHILE IS_SELECT_MENU_JUST_PRESSED
            WAIT 0
        ENDWHILE
        GET_LABEL_POINTER Aioptions almacen
        READ_MEMORY almacen 4 0 coords
        IF coords=0
            idd="~y~Default"
        ELSE
            idd="~y~Direct"
        ENDIF
        almacen+=4
        READ_MEMORY almacen 4 0 selected
        RETURN
        PRINT_FORMATTED_NOW "Unknow error" 2000
        GOTO addcheckpoint
    //delete checkpoint menu
    delcheckpoint:
        CREATE_MENU RCHFTIT (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
        SET_MENU_COLUMN menu 0 DUMMY (RCDFDEL RCHFEXI DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
        WHILE TRUE
            WAIT 0
            GOSUB testmenu
            IF IS_SELECT_MENU_JUST_PRESSED
                GET_MENU_ITEM_SELECTED menu (selected)
                SWITCH selected
                    CASE 0
                        IF i=-1
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 4) ()
                            PRINT_FORMATTED_NOW "There are not checkpoints!" 1000
                            DELETE_MENU menu
                            GOTO delcheckpoint
                        ENDIF
                        GOSUB delch
                        BREAK
                    CASE 1
                        WHILE IS_SELECT_MENU_JUST_PRESSED
                            WAIT 0
                        ENDWHILE
                        DELETE_MENU menu
                        GOTO checkpointmenu
                        BREAK
                ENDSWITCH
            ENDIF
        ENDWHILE    
    //
    delch:
        IF i=0
            almacen=0
            GET_LABEL_POINTER Checkpoint almacen
            READ_MEMORY almacen 4 FALSE checkp
            DELETE_CHECKPOINT checkp
            GET_LABEL_POINTER Checkpoint almacen
            almacen+=4
            READ_MEMORY almacen 4 FALSE blip
            REMOVE_BLIP blip
            GET_LABEL_POINTER Dumper filename
            idd="DELETED"
            WRITE_STRING_TO_INI_FILE $idd $filename "Checkpoints" "0"
            i-=1
        ELSE
            DELETE_CHECKPOINT checkp
            REMOVE_BLIP blip
            GET_LABEL_POINTER Istring selected
            STRING_FORMAT selected "%i" i
            GET_LABEL_POINTER Dumper filename
            idd="DELETED"
            WRITE_STRING_TO_INI_FILE $idd $filename "Checkpoints" $selected
            IF i>1
                GET_LABEL_POINTER Checkpoint coords
                coords+=8
                READ_MEMORY coords 4 0 checkp
                DELETE_CHECKPOINT checkp
                coords+=4
                READ_MEMORY coords 4 0 blip
                REMOVE_BLIP blip
            ENDIF  
            i-=1
            IF NOT i=0
                IF i>1
                    i-=1
                    GET_LABEL_POINTER Istring selected
                    STRING_FORMAT selected "%i" i
                    GET_LABEL_POINTER Coords coords
                    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                    SCAN_STRING $coords "%f %f %f %f %i %f" offs x y z radio selected angle
                    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                    IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                        CREATE_CHECKPOINT 3 x y z x y z radio checkp
                    ELSE
                        CREATE_CHECKPOINT 0 x y z x y z radio checkp
                    ENDIF
                    ADD_BLIP_FOR_COORD x y z blip
                    CHANGE_BLIP_COLOUR blip 0
                    GET_LABEL_POINTER Checkpoint coords
                    coords+=8
                    WRITE_MEMORY coords 4 checkp 0
                    coords+=4
                    WRITE_MEMORY coords 4 blip 0
                    i+=1
                ENDIF
                GET_LABEL_POINTER Istring selected
                STRING_FORMAT selected "%i" i
                GET_LABEL_POINTER Coords coords
                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                SCAN_STRING $coords "%f %f %f %f %i %f" offs x y z radio selected angle
                READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                    CREATE_CHECKPOINT 3 x y z x y z radio checkp
                ELSE
                    CREATE_CHECKPOINT 0 x y z x y z radio checkp
                ENDIF
                ADD_BLIP_FOR_COORD x y z blip
                CHANGE_BLIP_COLOUR blip 0
            ENDIF
        ENDIF
        WHILE IS_SELECT_MENU_JUST_PRESSED
            WAIT 0
        ENDWHILE
        RETURN
    //
    //
    chauto:

        
        READ_FLOAT_FROM_INI_FILE "CLEO/Race Creator++/General settings.ini" "Settings" "AutoCreationTime" x
        offs=1
        WHILE offs=1
            WAIT 0
            PRINT_FORMATTED_NOW "This tool will create a checkpoint every ~y~%f~w~ seconds. Press space to continue, enter to cancel" 100 x

            IF IS_KEY_JUST_PRESSED VK_SPACE
                offs=0
            ENDIF

            IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                RETURN
            ENDIF
        ENDWHILE
        PRINT_HELP_FORMATTED "Press ~y~X~w~ to stop~n~Press ~y~+~w~ and ~y~-~w~ to change time lapse"
        y=x*1000.0
        offs=#y
        GET_LABEL_POINTER AutoCh coords
        WRITE_MEMORY coords 4 x 0
        coords+=4
        WRITE_MEMORY coords 4 y 0
        coords+=4
        WRITE_MEMORY coords 4 offs 0
        coords+=4
        WRITE_MEMORY coords 4 0 0
        STREAM_CUSTOM_SCRIPT_FROM_LABEL loopauto
        WHILE TRUE
            WAIT offs
            GET_LABEL_POINTER Aioptions coords
            coords+=4
            WRITE_MEMORY coords 4 selected 0
            GOSUB crearcheck
            almacen=0
            GET_LABEL_POINTER Coords coords
            STRING_FORMAT coords "Total:~y~%i" i
            ADD_TEXT_LABEL RMFC-C1 $coords
            
            
            

            GET_LABEL_POINTER AutoCh coords
            READ_MEMORY coords 4 0 x
            coords+=4
            READ_MEMORY coords 4 0 y
            coords+=4
            READ_MEMORY coords 4 0 offs
            coords+=4
            READ_MEMORY coords 4 0 coords
            IF coords=1
                RETURN
            ENDIF 
        ENDWHILE
        RETURN
    //
    loopauto:
        GET_LABEL_POINTER AutoCh coords
        READ_MEMORY coords 4 0 x
        coords+=4
        READ_MEMORY coords 4 0 y
        coords+=4
        READ_MEMORY coords 4 0 offs

        WHILE TRUE
            WAIT 0
            //GOSUB showRaceInfo
            IF IS_KEY_JUST_PRESSED VK_KEY_X
                GET_LABEL_POINTER AutoCh coords
                coords+=12
                WRITE_MEMORY coords 4 1 0
                TERMINATE_THIS_CUSTOM_SCRIPT
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_ADD
                x+=0.1
                y=x*1000.0
                offs=#y
                GET_LABEL_POINTER AutoCh coords
                WRITE_MEMORY coords 4 x 0
                coords+=4
                WRITE_MEMORY coords 4 y 0
                coords+=4
                WRITE_MEMORY coords 4 offs 0
            ENDIF
            PRINT_FORMATTED_NOW "Creating checkpoint every ~y~%f~w~ seconds" 100 x
            IF IS_KEY_JUST_PRESSED VK_SUBTRACT
                IF NOT x<0.2
                    x-=0.1
                    y=x*1000.0
                    offs=#y
                    GET_LABEL_POINTER AutoCh coords
                    WRITE_MEMORY coords 4 x 0
                    coords+=4
                    WRITE_MEMORY coords 4 y 0
                    coords+=4
                    WRITE_MEMORY coords 4 offs 0
                ENDIF
            ENDIF
        ENDWHILE
        GOTO loopauto


    //
    radioAdd:
        IF NOT radio>29.0
            radio+=1.0
            GET_AUDIO_SFX_VOLUME vol
            GET_LABEL_POINTER Coords coords
            offs=#radio
            STRING_FORMAT coords "~g~%i" offs
            ADD_TEXT_LABEL RMFFME3 $coords
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 1
        ENDIF
        RETURN
    radioLow:
        IF NOT radio<2.0
            radio-=1.0
            GET_AUDIO_SFX_VOLUME vol
            GET_LABEL_POINTER Coords coords
            offs=#radio
            STRING_FORMAT coords "~g~%i" offs
            ADD_TEXT_LABEL RMFFME3 $coords
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 2
        ENDIF
        RETURN
    
    //
    behChange:
        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
        IF IS_STRING_EQUAL $idd "STREET" 7 0 "z"
            GET_LABEL_POINTER Aioptions offs
            READ_MEMORY offs 4 0 coords
            IF coords=0
                idd="~g~Direct"
                WRITE_MEMORY offs 4 1 0
            ELSE
                idd="~g~Default"
                WRITE_MEMORY offs 4 0 0    
            ENDIF
            GET_AUDIO_SFX_VOLUME vol
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 1)
            ADD_TEXT_LABEL RMFFME0 $idd                
        ELSE
            PRINT_FORMATTED_NOW "~r~AI behavior must be direct on air and sea races." 1000
            GET_AUDIO_SFX_VOLUME angle
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 15)
            WAIT 1000
            idd="~g~Direct"
        ENDIF
        RETURN
    //
    speedAdd:
        GET_LABEL_POINTER Aioptions coords
        coords+=4
        READ_MEMORY coords 4 0 selected
        IF NOT selected>199
            selected+=1
            WRITE_MEMORY coords 4 selected 0
        ENDIF
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "~g~%i" selected
        ADD_TEXT_LABEL RMFFME1 $coords
        GET_AUDIO_SFX_VOLUME vol
        CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 1)
        RETURN
    speedLow:
        GET_LABEL_POINTER Aioptions coords
        coords+=4
        READ_MEMORY coords 4 0 selected
        IF NOT selected<2
            selected-=1
            WRITE_MEMORY coords 4 selected 0
        ENDIF
        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "~g~%i" selected
        ADD_TEXT_LABEL RMFFME1 $coords
        GET_AUDIO_SFX_VOLUME vol
        CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 2)
        RETURN
    //
    changeVis:
        IF NOT i=-1
            GET_LABEL_POINTER Aioptions offs
            offs+=8
            READ_MEMORY offs 4 0 coords
            IF coords=0
                idd="~g~Default"
                WRITE_MEMORY offs 4 1 0
            ELSE
                IF coords=1
                    idd="~g~Teleporter"
                    WRITE_MEMORY offs 4 2 0
                ELSE
                    idd="~g~Guiding"
                    WRITE_MEMORY offs 4 0 0
                ENDIF    
            ENDIF
            GET_AUDIO_SFX_VOLUME vol
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 1)
            ADD_TEXT_LABEL RMFFME2 $idd                
        ELSE
            PRINT_FORMATTED_NOW "~r~Checkpoint 0 must be default!" 1000
            GET_AUDIO_SFX_VOLUME vol
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 15)
            WAIT 1000
            //idd="On"
        ENDIF
        RETURN
    //
    tractionAdd:
        IF NOT traccion>9.9
            traccion+=0.1
        ENDIF
        GET_AUDIO_SFX_VOLUME vol
        CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 1
        RETURN
    tractionLow:
        IF NOT traccion<0.2
            traccion-=0.1
        ENDIF
        GET_AUDIO_SFX_VOLUME vol
        CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 1
        RETURN
    //
    extops:
        SET_PLAYER_CONTROL 0 0
        GOSUB checkplane
        CREATE_MENU RCFOPTI (30.0 170.0) (100.0) 1 TRUE TRUE 0 (menu)
        SET_MENU_COLUMN menu 0 DUMMY (RCFAIBE RCFAISP RCHFEXI DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
        WHILE TRUE
            WAIT 0
            GET_MENU_ITEM_SELECTED menu selected
            IF selected=0
                PRINT_FORMATTED_NOW "Choose if the opponents will go directly to the checkpoint or following paths" 50
            ENDIF
            IF selected=1
                PRINT_FORMATTED_NOW "Choose the speed limit of opponents to reach the checkpoint. Useful in sharp turns" 50
            ENDIF
            IF IS_SELECT_MENU_JUST_PRESSED
                GET_MENU_ITEM_SELECTED menu selected
                SWITCH selected
                    CASE 0
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "STREET" 8 0 "z"
                            DELETE_MENU menu
                            CREATE_MENU RCFOPTI (30.0 170.0) (100.0) 1 TRUE TRUE 0 (menu)
                            SET_MENU_COLUMN menu 0 DUMMY (RCFADVC RCFAGXX RCHFEXI DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
                            WHILE TRUE
                                WAIT 0
                                GET_MENU_ITEM_SELECTED menu selected
                                IF selected=0
                                    PRINT_FORMATTED "Opponents will follow paths to reach the checkpoint" 50
                                ENDIF
                                IF selected=1
                                    PRINT_FORMATTED "Opponents will go directly to the checkpoint, ignoring paths. Mandatory is a section of your track will be off-paths." 50
                                ENDIF
                                IF IS_SELECT_MENU_JUST_PRESSED
                                    GET_MENU_ITEM_SELECTED menu selected
                                    SWITCH selected
                                        CASE 0
                                            GET_LABEL_POINTER Aioptions almacen
                                            WRITE_MEMORY almacen 4 0 0
                                            PRINT_FORMATTED_NOW "Default selected" 1000
                                            BREAK
                                        CASE 1
                                            GET_LABEL_POINTER Aioptions almacen
                                            WRITE_MEMORY almacen 4 1 0
                                            PRINT_FORMATTED_NOW "Direct selected" 1000
                                            BREAK
                                        CASE 2
                                            DELETE_MENU menu
                                            GOTO extops
                                            BREAK
                                    ENDSWITCH
                                ENDIF
                            ENDWHILE
                        ELSE
                            PRINT_FORMATTED_NOW "~r~AI behavior must be direct on air and sea races." 1000
                            idd="Direct"
                            GET_AUDIO_SFX_VOLUME angle
                            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 14)
                            WAIT 1000
                        ENDIF
                        BREAK
                    CASE 1
                        DELETE_MENU menu
                        GET_LABEL_POINTER Aioptions almacen
                        almacen+=4
                        READ_MEMORY almacen 4 0 selected 
                        PRINT_HELP_FOREVER RCFAIOI
                        WHILE TRUE
                            WAIT 0
                            PRINT_FORMATTED_NOW "Speed:%i" 50 selected
                            IF IS_KEY_PRESSED VK_ADD
                                IF NOT selected>199
                                    selected+=1
                                ENDIF
                            ENDIF
                            IF IS_KEY_PRESSED VK_SUBTRACT
                                IF NOT selected<2
                                    selected-=1
                                ENDIF
                            ENDIF
                            IF IS_KEY_JUST_PRESSED VK_SPACE  
                                WRITE_MEMORY almacen 4 selected 0
                                CLEAR_HELP
                                GOTO extops 
                            ENDIF                      
                        ENDWHILE
                        BREAK
                    CASE 2
                        DELETE_MENU menu
                        GOTO addcheckpoint
                        BREAK
                ENDSWITCH
            ENDIF
        ENDWHILE

    //edit ch
    editcheckpoint:
        SET_PLAYER_CONTROL 0 1
        GOSUB checkplane
        IF NOT almacen=0
            IF NOT i=0
                DELETE_CHECKPOINT checkp//ver
                REMOVE_BLIP blip
                GET_LABEL_POINTER Checkpoint selected
                READ_MEMORY selected 4 0 checkp
            ENDIF
        ENDIF
        IF isnew=1
            IF i>1
                GET_LABEL_POINTER Checkpoint coords
                coords+=8
                READ_MEMORY coords 4 0 checkp
                DELETE_CHECKPOINT checkp
                coords+=4
                READ_MEMORY coords 4 0 blip
                REMOVE_BLIP blip
                isnew=0
            ENDIF
        ENDIF
        almacen=i
        /*GET_LABEL_POINTER Checkpoint offs
        READ_MEMORY offs 4 0 coords
        DELETE_CHECKPOINT coords
        offs+=4
        READ_MEMORY offs 4 0 coords
        REMOVE_BLIP coords*/
        GET_LABEL_POINTER Istring offs
        GET_LABEL_POINTER Auxiliar coords
        coords+=4
        READ_MEMORY coords 4 0 selected
        // IF selected=-1
        //     GET_LABEL_POINTER Coords coords
        //     isTrue=1
        //     WHILE isTrue=1
        //         WAIT 0
        //         STRING_FORMAT offs "%i" almacen
        //         IF READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
        //         AND NOT IS_STRING_EQUAL $coords "DELETED" 7 0 "m"
        //             almacen+=1
        //         ELSE
        //             isTrue=0
        //             almacen-=1
        //             GET_LABEL_POINTER Auxiliar coords
        //             coords+=4
        //             WRITE_MEMORY coords 4 almacen 0
        //         ENDIF
        //     ENDWHILE
        // ELSE
        //     almacen=selected
        // ENDIF
        GET_LABEL_POINTER Auxiliar coords
        coords+=4
        WRITE_MEMORY coords 4 i 0
        GET_LABEL_POINTER Auxiliar coords
        READ_MEMORY coords 4 0 selected
        IF selected=-1
            STRING_FORMAT offs "%i" almacen
            isTrue=-1
        ELSE
            STRING_FORMAT offs "%i" selected
            isTrue=selected
        ENDIF
        GET_LABEL_POINTER Coords coords
        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $offs coords
        SCAN_STRING $coords "%f %f %f %f %i %f %i" selected x y z radio filen angle char
        IF NOT isTrue=0
            READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
            IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                CREATE_CHECKPOINT 4 x y z x y z radio checkp
            ELSE
                CREATE_CHECKPOINT 0 x y z x y z radio checkp
            ENDIF
            ADD_BLIP_FOR_COORD x y z blip
            CHANGE_BLIP_COLOUR blip 0
        ENDIF
        SET_CHAR_COORDINATES scplayer x y z
        IF isTrue=-1
            offs=almacen
        ELSE
            offs=isTrue
        ENDIF
        PRINT_HELP_FOREVER RCFECZX
        WHILE TRUE
            WAIT 0
            PRINT_FORMATTED_NOW "Checkpoint ~g~%i" 100 offs
            GET_LABEL_POINTER Coords selected
            STRING_FORMAT selected "~b~X~w~: %f" x
            DRAW_STRING_EXT $selected DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.6 0 1 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            STRING_FORMAT selected "~b~Y~w~: %f" y
            DRAW_STRING_EXT $selected DRAW_EVENT_BEFORE_HUD 55.0 260.0 0.2 0.6 0 1 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            STRING_FORMAT selected "~b~Z~w~: %f" z
            DRAW_STRING_EXT $selected DRAW_EVENT_BEFORE_HUD 55.0 270.0 0.2 0.6 0 1 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            STRING_FORMAT selected "~b~Radius~w~: %f" radio
            DRAW_STRING_EXT $selected DRAW_EVENT_BEFORE_HUD 55.0 280.0 0.2 0.6 0 1 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            
            IF filen=1
                STRING_FORMAT selected "~b~AI Behavior~w~: Direct"
            ELSE
                STRING_FORMAT selected "~b~AI Behavior~w~: Default"
            ENDIF
            DRAW_STRING_EXT $selected DRAW_EVENT_BEFORE_HUD 55.0 290.0 0.2 0.6 0 1 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            
            STRING_FORMAT selected "~b~AI Speed~w~: %f" angle
            DRAW_STRING_EXT $selected DRAW_EVENT_BEFORE_HUD 55.0 300.0 0.2 0.6 0 1 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

            IF IS_KEY_JUST_PRESSED VK_UP
                IF IS_KEY_PRESSED VK_LSHIFT
                    offs+=10
                    IF offs>almacen
                        offs=0
                    ENDIF
                ELSE
                    IF NOT offs=almacen
                        offs+=1
                    ELSE
                        offs=0
                    ENDIF
                ENDIF
                GET_LABEL_POINTER Istring selected
                STRING_FORMAT selected "%i" offs
                GET_LABEL_POINTER Coords coords
                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                SCAN_STRING $coords "%f %f %f %f %i %f %i %f" selected x y z radio filen angle char traccion
                IF NOT offs=1
                    DELETE_CHECKPOINT checkp
                    REMOVE_BLIP blip
                ENDIF
                IF NOT offs=0
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                            CREATE_CHECKPOINT 4 x y z x y z radio checkp
                        ELSE
                            CREATE_CHECKPOINT 0 x y z x y z radio checkp
                        ENDIF
                        ADD_BLIP_FOR_COORD x y z blip
                        CHANGE_BLIP_COLOUR blip 0
                    ENDIF
                    IF IS_CHAR_IN_ANY_CAR scplayer
                        GET_CAR_CHAR_IS_USING scplayer car
                        SET_CAR_COORDINATES car x y z
                    ELSE
                        SET_CHAR_COORDINATES scplayer x y z
                    ENDIF
                /*ELSE
                    GET_AUDIO_SFX_VOLUME x2
                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 x2 4) ()
                ENDIF*/
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_DOWN
                IF IS_KEY_PRESSED VK_LSHIFT
                    offs-=10
                    IF offs<0
                        offs=0
                    ENDIF
                ELSE
                    IF NOT offs=0
                        offs-=1
                    ELSE
                        offs=almacen
                    ENDIF
                ENDIF
                    GET_LABEL_POINTER Istring selected
                    STRING_FORMAT selected "%i" offs
                    GET_LABEL_POINTER Coords coords
                    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                    SCAN_STRING $coords "%f %f %f %f %i %f %i" selected x y z radio filen angle char
                    IF NOT offs=almacen
                        DELETE_CHECKPOINT checkp
                        REMOVE_BLIP blip
                    ENDIF
                    IF NOT offs=0
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                            CREATE_CHECKPOINT 4 x y z x y z radio checkp
                        ELSE
                            CREATE_CHECKPOINT 0 x y z x y z radio checkp
                        ENDIF
                        ADD_BLIP_FOR_COORD x y z blip
                        CHANGE_BLIP_COLOUR blip 0
                    ENDIF
                    IF IS_CHAR_IN_ANY_CAR scplayer
                        GET_CAR_CHAR_IS_USING scplayer car
                        SET_CAR_COORDINATES car x y z
                    ELSE
                        SET_CHAR_COORDINATES scplayer x y z
                    ENDIF
                    
                /*ELSE
                    GET_AUDIO_SFX_VOLUME x2
                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 x2 4) ()
                ENDIF*/
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_SPACE
                almacen=offs
                isTrue=offs
                GET_LABEL_POINTER Auxiliar selected
                WRITE_MEMORY selected 4 offs 0
                GOTO edmenu
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_RETURN
                
                GET_LABEL_POINTER Istring selected
                STRING_FORMAT selected "%i" almacen
                IF NOT offs=0
                    DELETE_CHECKPOINT checkp
                    REMOVE_BLIP blip
                ENDIF
                IF i>1
                    i-=1
                    GET_LABEL_POINTER Istring selected
                    STRING_FORMAT selected "%i" i
                    GET_LABEL_POINTER Coords coords
                    READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                    SCAN_STRING $coords "%f %f %f" selected x y angle
                    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                    IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
                        CREATE_CHECKPOINT 4 x y z x y z radio checkp
                    ELSE
                        CREATE_CHECKPOINT 0 x y z x y z radio checkp
                    ENDIF
                    ADD_BLIP_FOR_COORD x y z blip
                    CHANGE_BLIP_COLOUR blip 0
                    GET_LABEL_POINTER Checkpoint coords
                    coords+=8
                    WRITE_MEMORY coords 4 checkp 0
                    coords+=4
                    WRITE_MEMORY coords 4 blip 0
                    i+=1
                ENDIF
                GET_LABEL_POINTER Istring selected
                STRING_FORMAT selected "%i" almacen
                GET_LABEL_POINTER Coords coords
                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z radio filen angle char
                READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                    CREATE_CHECKPOINT 4 x y z x y z radio checkp
                ELSE
                    CREATE_CHECKPOINT 0 x y z x y z radio checkp
                ENDIF
                ADD_BLIP_FOR_COORD x y z blip
                CHANGE_BLIP_COLOUR blip 0
                CLEAR_HELP
                GOTO checkpointmenu
            ENDIF    
        ENDWHILE

    //
    edmenu:
        SET_PLAYER_CONTROL 0 0
        GOSUB checkplane
        CLEAR_HELP

        GET_LABEL_POINTER Istring selected
        STRING_FORMAT selected "%i" almacen
        GET_LABEL_POINTER Coords coords
        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
        SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" offs x y z radio filen angle char traccion w
        GET_LABEL_POINTER Aioptions offs
        IF filen=0
            idd="~g~Default"
            WRITE_MEMORY offs 4 0 0
        ELSE
            idd="~g~Direct"
            WRITE_MEMORY offs 4 1 0
        ENDIF

        ADD_TEXT_LABEL RMFFME0 $idd

        GET_LABEL_POINTER Coords coords
        STRING_FORMAT coords "~g~%.1f" angle
        ADD_TEXT_LABEL RMFFME1 $coords

        GET_LABEL_POINTER Aioptions offs
        offs+=8
        IF char=0
            STRING_FORMAT coords "~g~Guiding"
            WRITE_MEMORY offs 4 0 0
        ELSE
            IF char=1
                STRING_FORMAT coords "~g~Default"
                WRITE_MEMORY offs 4 1 0
            ELSE
                STRING_FORMAT coords "~g~Teleporter"
                WRITE_MEMORY offs 4 2 0
            ENDIF
        ENDIF
        ADD_TEXT_LABEL RMFFME2 $coords

        GET_LABEL_POINTER Coords coords
        offs=#radio
        STRING_FORMAT coords "~g~%i" offs
        ADD_TEXT_LABEL RMFFME3 $coords

        STRING_FORMAT coords "~g~%.2f" traccion
        ADD_TEXT_LABEL RMFFME4 $coords

        menu=0
        WHILE TRUE
            WAIT 0

            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 120.0 120.0 30.0 180.0 0.0 0 0 0 120 120 120 220

            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 215.0 120.0 215.0 0.0 0.0 0 0 0 0 0 60 100//coso grandee
            DRAW_STRING_EXT "Editing" DRAW_EVENT_BEFORE_HUD 80.0 110.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 255 79 28 200
            
            DRAW_RECT 80.0 105.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 135.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 320.0 120.0 3.0 50 50 50 255

            DRAW_STRING_EXT "Controls:" DRAW_EVENT_AFTER_DRAWING 555.0 340.0 0.15 0.6 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~NUM8-2 / Up-Down Arrows~w~: Navigate" DRAW_EVENT_BEFORE_HUD 555.0 360.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~NUM4-6 / Left-Right Arrows~w~: Change value" DRAW_EVENT_BEFORE_HUD 555.0 375.3 0.14 0.5 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~NUM5 / Space~w~: Select" DRAW_EVENT_BEFORE_HUD 555.0 389.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~Keep H~w~: Show help" DRAW_EVENT_BEFORE_HUD 555.0 404.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200
            DRAW_STRING_EXT "~y~Z~w~: Hide menu" DRAW_EVENT_BEFORE_HUD 555.0 419.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
            DRAW_STRING_EXT "~y~Enter~w~: Exit" DRAW_EVENT_BEFORE_HUD 555.0 434.0 0.15 0.6 0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 255 1 0 0 0 200

            SWITCH menu
                CASE 0
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 30 30 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200                    
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 197.0 60.0 15.0 0.0 0.0 0 0 0 0 0 0 200
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 140.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "Its the checkpoint size. Default is 10." 100
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_LEFT
                        GOSUB radioLow
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_RIGHT
                        GOSUB radioAdd
                    ENDIF
                    BREAK
                CASE 2
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 217.0 115.0 15.0 0.0 0.0 0 0 0 0 0 0 200
                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 60.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "If its ~g~Default~w~ then opponents they will follow paths. If~g~Direct~w~, then they will go directly to the checkpoint." 100
                    ENDIF

                    IF IS_KEY_JUST_PRESSED VK_LEFT
                    OR IS_KEY_JUST_PRESSED VK_RIGHT
                        GOSUB behChange
                    ENDIF
                    BREAK
                CASE 3
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 237.0 90.0 15.0 0.0 0.0 0 0 0 0 0 0 200
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "Opponents ~b~max speed~w~ to reach this checkpoint." 100
                    ENDIF

                    IF IS_KEY_PRESSED VK_RIGHT
                        IF NOT angle>199.0
                            angle+=1.0
                        ENDIF
                        GET_LABEL_POINTER Coords coords
                        STRING_FORMAT coords "~g~%.2f" angle
                        ADD_TEXT_LABEL RMFFME1 $coords
                        GET_AUDIO_SFX_VOLUME vol
                        CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 1)
                    ENDIF
                        
                    IF IS_KEY_PRESSED VK_LEFT
                        IF NOT angle<2.0
                            angle-=1.0
                        ENDIF
                        GET_LABEL_POINTER Coords coords
                        STRING_FORMAT coords "~g~%.2f" angle
                        ADD_TEXT_LABEL RMFFME1 $coords
                        GET_AUDIO_SFX_VOLUME vol
                        CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 vol 1)
                    ENDIF
                    BREAK
                CASE 4
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.5 257.0 90.0 15.0 0.0 0.0 0 0 0 0 0 0 200
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 37.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "~g~Guiding~w~:Only the opponents will se this checkpoint. ~g~Teleporter~w~:Teleports to the next checkpoint." 100
                    ENDIF
                    IF IS_KEY_JUST_PRESSED VK_LEFT
                    OR IS_KEY_JUST_PRESSED VK_RIGHT
                        GOSUB changeVis
                    ENDIF
                    BREAK
                CASE 5
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.5 277.0 80.0 15.0 0.0 0.0 0 0 0 0 0 0 200
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    
                    IF IS_KEY_PRESSED VK_KEY_H
                        PRINT_FORMATTED_NOW "The ~b~traction control~w~ is how well the opponents will handle the car to reach this checkpoint. Default value is 1.0" 100
                    ENDIF

                    IF IS_KEY_PRESSED VK_LEFT
                        GOSUB tractionLow
                        GET_LABEL_POINTER Coords coords
                        STRING_FORMAT coords "~g~%.2f" traccion
                        ADD_TEXT_LABEL RMFFME4 $coords
                    ENDIF
                        
                    IF IS_KEY_PRESSED VK_RIGHT
                        GOSUB tractionAdd
                        GET_LABEL_POINTER Coords coords
                        STRING_FORMAT coords "~g~%.2f" traccion
                        ADD_TEXT_LABEL RMFFME4 $coords
                    ENDIF
                    BREAK
                CASE 6
                    DRAW_STRING_EXT "Change position" DRAW_EVENT_BEFORE_HUD 80.0 150.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Radius:" DRAW_EVENT_BEFORE_HUD 75.0 190.0 0.2 0.7 0 2 1 0 42.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME3 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Behaviour:" DRAW_EVENT_BEFORE_HUD 60.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    
                    DRAW_STRING_EXT "Max speed:" DRAW_EVENT_BEFORE_HUD 70.0 230.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 108.0 230.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Type:" DRAW_EVENT_BEFORE_HUD 55.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 100.0 250.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    DRAW_STRING_EXT "Traction:" DRAW_EVENT_BEFORE_HUD 70.0 270.0 0.2 0.7  0 2 1 0 45.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME4 idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 105.0 270.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 290.0 0.2 0.7  0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 1 100 70 70 200
                    BREAK
            ENDSWITCH

            IF IS_KEY_JUST_PRESSED VK_DOWN
            OR IS_KEY_JUST_PRESSED VK_KEY_S
            OR IS_KEY_JUST_PRESSED VK_NUMPAD2
                IF menu=6
                    menu=0
                ELSE
                    menu+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_UP
            OR IS_KEY_JUST_PRESSED VK_KEY_W
            OR IS_KEY_JUST_PRESSED VK_NUMPAD8
                IF menu=0
                    menu=6
                ELSE
                    menu-=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3 //CAMBIAR
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_SPACE
            OR IS_KEY_JUST_PRESSED VK_NUMPAD5
                SWITCH menu
                    CASE 0
                        GET_LABEL_POINTER Istring selected
                        STRING_FORMAT selected "%i" almacen
                        GOSUB writeToIni
                        SET_PLAYER_CONTROL 0 1
                        DELETE_MENU menu
                        PRINT_HELP_FOREVER RCFXPLK
                        WHILE TRUE
                            WAIT 0
                            IF IS_KEY_JUST_PRESSED VK_SPACE
                                GET_LABEL_POINTER Istring selected
                                STRING_FORMAT selected "%i" almacen
                                GET_LABEL_POINTER Coords coords
                                READ_STRING_FROM_INI_FILE $filename "Checkpoints" $selected coords
                                SCAN_STRING $coords "%f %f %f %f %i %f %i %f %f" offs x y z radio filen angle char traccion w
                                GET_CHAR_COORDINATES scplayer x y z
                                IF IS_CHAR_IN_ANY_CAR scplayer
                                    GET_CAR_CHAR_IS_USING scplayer car
                                    GET_CAR_ROLL car w
                                ELSE
                                    w=0.0
                                ENDIF
                                GET_LABEL_POINTER Coords coords
                                STRING_FORMAT coords "%f %f %f %f %i %f %i %.2f %.2f" x y z radio filen angle char traccion w
                                WRITE_STRING_TO_INI_FILE $coords $filename "Checkpoints" $selected
                                IF almacen=0
                                    GET_LABEL_POINTER Checkpoint offs
                                    READ_MEMORY offs 4 0 checkp
                                    DELETE_CHECKPOINT checkp
                                    CREATE_CHECKPOINT 1 x y z x y z radio checkp
                                    WRITE_MEMORY offs 4 checkp 0
                                    offs+=4
                                    READ_MEMORY offs 4 0 blip
                                    REMOVE_BLIP blip
                                    ADD_BLIP_FOR_COORD x y z blip
                                    CHANGE_BLIP_COLOUR blip 0
                                    WRITE_MEMORY offs 4 blip 0
                                    PRINT_FORMATTED_NOW "Position changed!" 1000
                                    WAIT 500
                                ELSE
                                    DELETE_CHECKPOINT checkp
                                    READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                                    IF IS_STRING_EQUAL $idd "AIR" 4 0 "z"
                                        CREATE_CHECKPOINT 4 x y z x y z radio checkp
                                    ELSE
                                        CREATE_CHECKPOINT 0 x y z x y z radio checkp
                                    ENDIF
                                    REMOVE_BLIP blip
                                    ADD_BLIP_FOR_COORD x y z blip
                                    CHANGE_BLIP_COLOUR blip 0
                                    PRINT_FORMATTED_NOW "Position changed!" 1000
                                    WAIT 500
                                ENDIF
                            ENDIF  
                            IF IS_KEY_JUST_PRESSED VK_RETURN
                                CLEAR_HELP
                                GOTO edmenu
                            ENDIF
                        ENDWHILE
                        BREAK
                    
                    CASE 6
                        GOSUB writeToIni
                        
                        REMOVE_TEXT_LABEL RMFFME0
                        REMOVE_TEXT_LABEL RMFFME1
                        REMOVE_TEXT_LABEL RMFFME2
                        REMOVE_TEXT_LABEL RMFFME3
                        REMOVE_TEXT_LABEL RMFFME4

                        IF NOT almacen=0
                            DELETE_CHECKPOINT checkp
                            REMOVE_BLIP blip
                        ENDIF
                        GOTO editcheckpoint
                        BREAK    
                ENDSWITCH
            ENDIF
        ENDWHILE
    //
    writeToIni:
        GET_LABEL_POINTER Coords coords
        GET_LABEL_POINTER Aioptions offs
        READ_MEMORY offs 4 0 filen
        offs+=8
        READ_MEMORY offs 4 0 char
        STRING_FORMAT coords "%f %f %f %f %i %f %i %.2f %f" x y z radio filen angle char traccion w
        WRITE_STRING_TO_INI_FILE $coords $filename "Checkpoints" $selected
        RETURN
    //
    checkplane:
        IF IS_CHAR_IN_ANY_CAR scplayer
            GET_CAR_CHAR_IS_USING scplayer car
            GET_VEHICLE_SUBCLASS car selected
            IF selected=VEHICLE_SUBCLASS_PLANE
            OR selected=VEHICLE_SUBCLASS_FPLANE
                FREEZE_CAR_POSITION car 1
            ENDIF
        ENDIF
        RETURN
    //
    checkplane2:
        IF IS_CHAR_IN_ANY_CAR scplayer
            GET_CAR_CHAR_IS_USING scplayer car
            FREEZE_CAR_POSITION car 0
        ENDIF
        RETURN
    //
    opsmenu:
        WAIT 0
        SET_PLAYER_ENTER_CAR_BUTTON 0 0
        SET_PLAYER_CONTROL 0 0

        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
        IF NOT IS_STRING_EQUAL $idd "SEA" 5 0 "z"
        AND NOT IS_STRING_EQUAL $idd "STREET" 7 0 "z"
        AND NOT IS_STRING_EQUAL $idd "AIR" 5 0 "z"
            idd="~g~STREET"
        ENDIF
        ADD_TEXT_LABEL RMFFME0 $idd

        READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
        IF offs=0
            idd="~g~Sprint"
        ELSE
            idd="~g~Circuit"
        ENDIF
        ADD_TEXT_LABEL RMFFME1 $idd

        READ_INT_FROM_INI_FILE $filename "Settings" "Reward" selected
        STRING_FORMAT idd "~g~$%i" selected
        ADD_TEXT_LABEL RMFFME2 $idd

        menu=0
        WHILE TRUE
            WAIT 0
            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 220.0 120.0 150.0 180.0 0.0 0 0 0 0 0 0 200
            DRAW_STRING_EXT "Options" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
            SWITCH menu
                CASE 0
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 197.0 115.0 15.0 0.0 0.0 0 0 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RCPFMAR idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 65.0 190.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200                    
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    GET_TEXT_LABEL_STRING RCPFTYP idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 63.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 107.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RC+OPFW idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 68.0 230.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 102.0 230.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RCFEXIT idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 80.0 260.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 217.0 115.0 15.0 0.0 0.0 0 0 0 0 0 0 200

                    GET_TEXT_LABEL_STRING RCPFMAR idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 65.0 190.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200                    
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    GET_TEXT_LABEL_STRING RCPFTYP idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 63.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 107.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RC+OPFW idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 68.0 230.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 102.0 230.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RCFEXIT idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 80.0 260.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 3

                    GET_TEXT_LABEL_STRING RCPFMAR idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 65.0 190.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200                    
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    GET_TEXT_LABEL_STRING RCPFTYP idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 63.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 107.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RC+OPFW idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 68.0 230.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 102.0 230.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RCFEXIT idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 80.0 260.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 70 70 200
                    BREAK
                CASE 2
                    DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 237.0 115.0 15.0 0.0 0.0 0 0 0 0 0 0 200

                    GET_TEXT_LABEL_STRING RCPFMAR idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 65.0 190.0 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200                    
                    GET_TEXT_LABEL_STRING RMFFME0 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 110.0 190.0 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200

                    GET_TEXT_LABEL_STRING RCPFTYP idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 63.0 210.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME1 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 107.0 210.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RC+OPFW idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 68.0 230.1 0.2 0.7 0 2 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    GET_TEXT_LABEL_STRING RMFFME2 idd
                    
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 102.0 230.1 0.2 0.7 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 0 0 0 0 200
                    
                    GET_TEXT_LABEL_STRING RCFEXIT idd
                    DRAW_STRING_EXT $idd DRAW_EVENT_BEFORE_HUD 80.0 260.0 0.2 0.7  0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    PRINT_FORMATTED_NOW "Press ~y~A/D ~w~or ~y~Arrows~w~ to change the value. Keep ~y~SHIFT ~w~or ~y~CTRL~w~ to do it faster/slower." 100
                    BREAK
            ENDSWITCH

            IF IS_KEY_JUST_PRESSED VK_DOWN
                IF menu=3
                    menu=0
                ELSE
                    menu+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF
            IF IS_KEY_JUST_PRESSED VK_UP
                IF menu=0
                    menu=3
                ELSE
                    menu-=1
                ENDIF
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3 //CAMBIAR
            ENDIF

            IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                REMOVE_TEXT_LABEL RMFFME1
                REMOVE_TEXT_LABEL RMFFME0
                GOTO createrace
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_LEFT
            OR IS_KEY_JUST_PRESSED VK_KEY_A
                SWITCH menu
                    CASE 0
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "STREET" 5 0 "z"
                            idd="AIR"
                            ADD_TEXT_LABEL RMFFME0 $idd
                            WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                        ELSE
                            IF IS_STRING_EQUAL $idd "AIR" 5 0 "z"
                                idd="SEA"
                                ADD_TEXT_LABEL RMFFME0 $idd
                                WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                            ELSE
                                idd="STREET"
                                ADD_TEXT_LABEL RMFFME0 $idd
                                WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                            ENDIF
                        ENDIF
                        BREAK
                    CASE 1 //track type
                        READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
                        IF offs=1
                            idd="~g~Sprint"
                            ADD_TEXT_LABEL RMFFME1 $idd
                            WRITE_INT_TO_INI_FILE 0 $filename "Settings" "IsCircuit"
                        ELSE
                            idd="~g~Circuit"
                            ADD_TEXT_LABEL RMFFME1 $idd
                            WRITE_INT_TO_INI_FILE 1 $filename "Settings" "IsCircuit"
                        ENDIF
                        ADD_TEXT_LABEL RMFFME1 $idd
                        BREAK
                    CASE 2
                        IF IS_KEY_PRESSED VK_LSHIFT
                            selected-=10000
                        ELSE
                            IF IS_KEY_PRESSED VK_OEM_PERIOD
                                selected-=100
                            ELSE
                                selected-=1000
                            ENDIF
                        ENDIF
                        IF selected<1
                            selected=0
                        ENDIF
                        STRING_FORMAT idd "~g~$%i" selected
                        ADD_TEXT_LABEL RMFFME2 $idd
                        WRITE_INT_TO_INI_FILE selected $filename "Settings" "Reward"
                        BREAK
                ENDSWITCH
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_RIGHT
            OR IS_KEY_JUST_PRESSED VK_KEY_D
                SWITCH menu
                    CASE 0
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "STREET" 5 0 "z"
                            idd="SEA"
                            ADD_TEXT_LABEL RMFFME0 $idd
                            WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                        ELSE
                            IF IS_STRING_EQUAL $idd "AIR" 5 0 "z"
                                idd="STREET"
                                ADD_TEXT_LABEL RMFFME0 $idd
                                WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                            ELSE
                                idd="AIR"
                                ADD_TEXT_LABEL RMFFME0 $idd
                                WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                            ENDIF
                        ENDIF
                        BREAK
                    CASE 1 //track type
                        READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
                        IF offs=1
                            idd="~g~Sprint"
                            ADD_TEXT_LABEL RMFFME1 $idd
                            WRITE_INT_TO_INI_FILE 0 $filename "Settings" "IsCircuit"
                        ELSE
                            idd="~g~Circuit"
                            ADD_TEXT_LABEL RMFFME1 $idd
                            WRITE_INT_TO_INI_FILE 1 $filename "Settings" "IsCircuit"
                        ENDIF
                        ADD_TEXT_LABEL RMFFME1 $idd
                        BREAK
                    CASE 2
                        IF IS_KEY_PRESSED VK_LSHIFT
                            selected+=10000
                        ELSE
                            IF IS_KEY_PRESSED VK_OEM_PERIOD
                                selected+=100
                            ELSE
                                selected+=1000
                            ENDIF
                        ENDIF
                        IF selected>1000000
                            selected=1000000
                        ENDIF
                        STRING_FORMAT idd "~g~$%i" selected
                        ADD_TEXT_LABEL RMFFME2 $idd
                        WRITE_INT_TO_INI_FILE selected $filename "Settings" "Reward"
                        BREAK
                ENDSWITCH
                GET_AUDIO_SFX_VOLUME vol
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 vol 3
            ENDIF

            

            IF IS_KEY_JUST_PRESSED VK_SPACE
                SWITCH menu
                    CASE 0
                        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
                        IF IS_STRING_EQUAL $idd "STREET" 5 0 "z"
                            idd="AIR"
                            ADD_TEXT_LABEL RMFFME0 $idd
                            WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                        ELSE
                            IF IS_STRING_EQUAL $idd "AIR" 5 0 "z"
                                idd="SEA"
                                ADD_TEXT_LABEL RMFFME0 $idd
                                WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                            ELSE
                                idd="STREET"
                                ADD_TEXT_LABEL RMFFME0 $idd
                                WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                            ENDIF
                        ENDIF
                        BREAK
                    CASE 1 //track type
                        READ_INT_FROM_INI_FILE $filename "Settings" "IsCircuit" offs
                        IF offs=1
                            idd="~g~Sprint"
                            ADD_TEXT_LABEL RMFFME1 $idd
                            WRITE_INT_TO_INI_FILE 0 $filename "Settings" "IsCircuit"
                        ELSE
                            idd="~g~Circuit"
                            ADD_TEXT_LABEL RMFFME1 $idd
                            WRITE_INT_TO_INI_FILE 1 $filename "Settings" "IsCircuit"
                        ENDIF
                        ADD_TEXT_LABEL RMFFME1 $idd
                        BREAK
                    CASE 3
                        REMOVE_TEXT_LABEL RMFFME2
                        REMOVE_TEXT_LABEL RMFFME1
                        REMOVE_TEXT_LABEL RMFFME0
                        GOTO createrace
                        BREAK  
                ENDSWITCH
            ENDIF
        ENDWHILE           
    //
    showRaceInfo:
        GET_STRING_LENGTH $name selected
        w=# selected
        w*=6.2
        
        DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 300.0 47.0 210.0 34.0 0.0 0.0 0 0 0 250 181 20 60

        DRAW_RECT 300.0 29.0 210.0 3.0 50 50 50 255
        DRAW_STRING_EXT $name DRAW_EVENT_BEFORE_HUD 300.0 30.0 0.25 0.75 0 2 1 0 w 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 210 //135 167 252

        GET_LABEL_POINTER Coords coords

        i+=1
        g+=1
        STRING_FORMAT coords "%i Checkpoints" i
        DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 260.0 50.0 0.2 0.7 0 1 1 0 70.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 210


        STRING_FORMAT coords "%i Racers" g
        DRAW_STRING_EXT $coords DRAW_EVENT_BEFORE_HUD 340.0 50.0 0.2 0.7 0 1 1 0 70.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 210

        i-=1
        g-=1
        DRAW_RECT 300.0 65.0 210.0 3.0 50 50 50 255

        DRAW_SPRITE 1 210.0 56.0 20.0 21.0 255 255 255 255 //circuito
        DRAW_SPRITE 2 393.0 54.0 21.0 21.0 255 255 255 255

        RETURN
    //
    testmenu:
            IF IS_KEY_JUST_PRESSED VK_LSHIFT
            AND IS_KEY_PRESSED VK_KEY_L
            AND NOT IS_CHAR_SITTING_IN_ANY_CAR scplayer
                GET_LABEL_POINTER Istring filen
                READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" filen
                IF IS_STRING_EQUAL $filen "STREET" 7 0 "m"
                    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestCarID" testcar
                ENDIF
                IF IS_STRING_EQUAL $filen "AIR" 7 0 "m"
                    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestAirID" testcar
                ENDIF
                IF IS_STRING_EQUAL $filen "SEA" 7 0 "m"
                    READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestSeaID" testcar
                ENDIF
                IF NOT IS_CHAR_SITTING_IN_ANY_CAR scplayer
                    GOSUB testcr
                ENDIF
            ENDIF
            RETURN
    //
    testcr://create test car
        REQUEST_MODEL testcar
        WHILE NOT HAS_MODEL_LOADED testcar
            WAIT 0
        ENDWHILE
        GET_CHAR_COORDINATES scplayer x y z
        CREATE_CAR testcar x y z testcar
        WHILE NOT DOES_VEHICLE_EXIST testcar
            WAIT 0
        ENDWHILE
        CHANGE_CAR_COLOUR testcar 1 1
        SET_CAR_CAN_BE_DAMAGED testcar 0
        SET_CAR_PROOFS testcar 1 1 1 1 1
        TASK_WARP_CHAR_INTO_CAR_AS_DRIVER scplayer testcar
        WAIT 100
        MARK_MODEL_AS_NO_LONGER_NEEDED testcar
        WAIT 2000
        RETURN

    //
    testdel://delete test car
        GET_CHAR_COORDINATES scplayer x y z
        GET_CAR_CHAR_IS_USING scplayer offs
        TASK_LEAVE_CAR_IMMEDIATELY scplayer offs
        WHILE IS_CHAR_IN_ANY_CAR scplayer
            WAIT 0
        ENDWHILE
        SET_CHAR_COORDINATES scplayer x y z
        DELETE_CAR offs
        WAIT 2000
        RETURN
    //
    racetype:
        SET_PLAYER_CONTROL 0 0
        CREATE_MENU RCPFTYP (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
        SET_MENU_COLUMN menu 0 DUMMY (RCFSTTT RCFAIRR RCFMARI RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
        WHILE TRUE
            WAIT 0
            IF IS_SELECT_MENU_JUST_PRESSED
                GET_MENU_ITEM_SELECTED menu selected
                SWITCH selected
                    CASE 0
                        PRINT_FORMATTED_NOW "Street race selected!" 1000
                        idd="STREET"
                        WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                        BREAK
                    CASE 1
                        PRINT_FORMATTED_NOW "Air race selected!" 1000
                        idd="AIR"
                        WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                        BREAK
                    CASE 2
                        PRINT_FORMATTED_NOW "Sea race selected!" 1000
                        idd="SEA"
                        WRITE_STRING_TO_INI_FILE $idd $filename "Settings" "Race type"
                        BREAK
                    CASE 3
                        DELETE_MENU menu
                        GOTO opsmenu
                        BREAK
                ENDSWITCH
            ENDIF
        ENDWHILE
    //
    tracktype:
        WAIT 0
        SET_PLAYER_CONTROL 0 0
        CREATE_MENU RCPFTYP (30.0 170.0) (180.0) 1 TRUE TRUE 0 (menu)
        SET_MENU_COLUMN menu 0 DUMMY (RCPFCIR RCPFSPR RCFEXIT DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY DUMMY)
        WHILE TRUE
            WAIT 0 
            IF IS_SELECT_MENU_JUST_PRESSED
                GET_MENU_ITEM_SELECTED menu (selected)
                SWITCH selected
                    CASE 0//circuit
                        WRITE_INT_TO_INI_FILE 1 $filename "Settings" "IsCircuit"
                        PRINT_FORMATTED_NOW "Circuit set" 1000
                        BREAK
                    CASE 1 //sprint
                        WRITE_INT_TO_INI_FILE 0 $filename "Settings" "IsCircuit"
                        PRINT_FORMATTED_NOW "Sprint set" 1000
                        BREAK
                    CASE 2 //exit
                        DELETE_MENU menu
                        GOTO opsmenu
                        BREAK  
                ENDSWITCH
            ENDIF
        ENDWHILE  

    //opponents menu
    oppmenu:
        SET_PLAYER_CONTROL 0 1
        GOSUB checkplane2
        READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestCarID" testcar
        LOAD_SPRITE 3 "pos_bg"
        menu=0
        WHILE TRUE
            WAIT 0

            GET_TEXTURE_FROM_SPRITE 3 coords
            DRAW_TEXTURE_PLUS coords DRAW_EVENT_BEFORE_HUD 80.0 190.0 120.0 140.0 180.0 0.0 0 0 0 255 255 255 200

            GOSUB showRaceInfo
            DRAW_STRING_EXT "Car positions" DRAW_EVENT_BEFORE_HUD 80.0 95.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 255 79 28 200
            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 105.0 120.0 30.0 180.0 0.0 0 0 0 120 120 120 220
            DRAW_RECT 80.0 90.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 120.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 260.0 120.0 3.0 50 50 50 255


            SWITCH menu
                CASE 0
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 190.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 220.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 190.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 220.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 2
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Delete last" DRAW_EVENT_BEFORE_HUD 80.0 190.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 220.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 160 30 30 200
                    BREAK
            ENDSWITCH
            
            IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
                GOTO createrace
            ENDIF
            GOSUB testmenu
            IF IS_KEY_JUST_PRESSED VK_SPACE
                SWITCH menu
                    CASE 0
                        WHILE IS_SELECT_MENU_JUST_PRESSED
                            WAIT 0
                        ENDWHILE     
                        IF NOT g>14           
                            GOSUB createpos
                        ELSE
                            PRINT_FORMATTED_NOW "You cant put more than 15 opponents!" 1000
                        ENDIF
                        BREAK
                    CASE 1
                        WHILE IS_SELECT_MENU_JUST_PRESSED
                            WAIT 0
                        ENDWHILE
                        IF NOT g=-1                
                            GOSUB deletepos
                        ELSE
                            PRINT_FORMATTED_NOW "You didnt added a position!" 1000
                        ENDIF
                        BREAK 
                    CASE 2
                        DELETE_MENU menu
                        CLEAR_ONSCREEN_COUNTER_LOCAL g
                        GOTO createrace
                        BREAK
                ENDSWITCH
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_DOWN
                IF menu=2
                    menu=0
                ELSE
                    menu+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ENDIF

            IF IS_KEY_JUST_PRESSED VK_UP
                IF menu=0
                    menu=2
                ELSE
                    menu-=1
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
            ENDIF
        ENDWHILE
        GOTO oppmenu
    //
    createpos:
        WAIT 0
        g=g+1
        almacen = 0
        GET_CHAR_COORDINATES scplayer x y z
        READ_STRING_FROM_INI_FILE $filename "Settings" "Race type" idd
        IF IS_STRING_EQUAL $idd "STREET" 7 0 "m"
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestCarID" testcar 
        ENDIF
        IF IS_STRING_EQUAL $idd "AIR" 7 0 "m"
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestAirID" testcar
        ENDIF
        IF IS_STRING_EQUAL $idd "SEA" 7 0 "m"
            READ_INT_FROM_INI_FILE "Cleo/Race Creator++/General settings.ini" "Settings" "TestSeaID" testcar
        ENDIF
        REQUEST_MODEL testcar
        WHILE NOT HAS_MODEL_LOADED testcar
            WAIT 0
        ENDWHILE
        IF IS_CHAR_IN_ANY_CAR scplayer
            GET_CAR_CHAR_IS_USING scplayer car
            GET_CAR_HEADING car angle
            GET_CAR_ROLL car radio
        ELSE
            GET_CHAR_HEADING scplayer angle
            radio=2.0
        ENDIF
        CREATE_CAR testcar x y z car
        MARK_MODEL_AS_NO_LONGER_NEEDED testcar
        IF IS_CHAR_IN_ANY_CAR scplayer
            SET_CAR_ROLL car radio
        ENDIF
        SET_CAR_HEADING car angle
        SET_CAR_PROOFS car 1 1 1 1 1
        
        FREEZE_CAR_POSITION_AND_DONT_LOAD_COLLISION car 1
        SET_CAR_COLLISION car 0
        
        IF g=0
            CHANGE_CAR_COLOUR car 5 5
            REPORT_MISSION_AUDIO_EVENT_AT_POSITION x y z SOUND_CHECKPOINT_RED
            PRINT_FORMATTED_NOW "You placed the first position. This is where the ~b~player~w~ will start the race." 1500
        ELSE                  
            IF g>0
                CHANGE_CAR_COLOUR car 2 2
                REPORT_MISSION_AUDIO_EVENT_AT_POSITION x y z SOUND_PICKUP_STANDARD
            ENDIF
        ENDIF

        
        LIST_ADD listaPos car
        GET_LABEL_POINTER Coords coords
    
        STRING_FORMAT coords "%f %f %f %f %f" x y z radio angle
        GET_LABEL_POINTER Istring selected

        STRING_FORMAT selected "%i" g
        WRITE_STRING_TO_INI_FILE $coords $filename "Vehicle Positions" $selected
        WHILE IS_SELECT_MENU_JUST_PRESSED
            WAIT 0
        ENDWHILE

        RETURN
    //
    
    deletepos:
        IF g=0
            almacen=0
            GET_LIST_VALUE_BY_INDEX listaPos 0 offs
            DELETE_CAR offs
            LIST_REMOVE_INDEX listaPos 0
            //REMOVE_SPHERE esfera
            idd="DELETED"
            WRITE_STRING_TO_INI_FILE $idd $filename "Vehicle Positions" "0"
            g-=1
        ELSE
            //REMOVE_SPHERE esfera
            GET_LIST_VALUE_BY_INDEX listaPos g offs
            DELETE_CAR offs
            LIST_REMOVE_INDEX listaPos g
            GET_LABEL_POINTER Istring selected
            STRING_FORMAT selected "%i" g
            idd="DELETED"
            WRITE_STRING_TO_INI_FILE $idd $filename "Vehicle Positions" $selected  
            g-=1
        ENDIF
        WHILE IS_SELECT_MENU_JUST_PRESSED
            WAIT 0
        ENDWHILE
        RETURN
    //
    stopc:
        WAIT 0
        SET_EVERYONE_IGNORE_PLAYER 0 0
        DO_FADE 200 0
        DISPLAY_ZONE_NAMES 1
        USE_TEXT_COMMANDS 0
        SET_TEXT_DRAW_BEFORE_FADE 0
        REMOVE_TEXTURE_DICTIONARY

        GET_LABEL_POINTER PlayerInt offs
        offs+=4
        READ_MEMORY offs 4 0 selected
        IF selected=-2
            offs=0
            GET_LABEL_POINTER PlayerInt offs
            READ_MEMORY offs 4 0 selected
            SET_AREA_VISIBLE selected
            SET_CHAR_AREA_VISIBLE scplayer selected
            IF IS_CHAR_IN_ANY_CAR scplayer
                GET_CAR_CHAR_IS_USING scplayer car
                SET_VEHICLE_AREA_VISIBLE car selected
            ENDIF
            GET_LABEL_POINTER PlayerCoords coords
            SCAN_STRING $coords "%f %f %f" offs x y z
            SET_CHAR_COORDINATES scplayer x y z 
        ENDIF
        WAIT 200
        GOSUB checkplane2
        DISABLE_ALL_ENTRY_EXITS 0
        SET_POLICE_IGNORE_PLAYER 0 0
        SET_CHAR_PROOFS scplayer 1 1 1 1 1
        SET_CAR_DENSITY_MULTIPLIER 1.0
        SET_PED_DENSITY_MULTIPLIER 1.0
        REMOVE_BLIP blip
        DELETE_CHECKPOINT checkp
        

        almacen=0
        GET_LABEL_POINTER Checkpoint almacen
        READ_MEMORY almacen 4 FALSE checkp
        DELETE_CHECKPOINT checkp

        GET_LABEL_POINTER Checkpoint almacen
        almacen+=4
        READ_MEMORY almacen 4 FALSE blip
        REMOVE_BLIP blip

        almacen+=4
        READ_MEMORY almacen 4 FALSE checkp
        DELETE_CHECKPOINT checkp

        almacen+=4
        READ_MEMORY almacen 4 FALSE blip
        REMOVE_BLIP blip

        almacen=0
        GET_LIST_SIZE listaPos offs
        WHILE almacen<offs
            GET_LIST_VALUE_BY_INDEX listaPos almacen car
            DELETE_CAR car
            LIST_REMOVE_INDEX listaPos almacen
            almacen+=1
        ENDWHILE
        
        CLEO_CALL deleteObjects 0 listaObjetos filename 0
        GOSUB deleteAllPos
        DELETE_LIST listaPos
        DELETE_LIST listaObjetos
        DO_FADE 200 1
        SET_PLAYER_CONTROL 0 1
        WAIT 1100
        SET_PLAYER_ENTER_CAR_BUTTON 0 1
        RETURN
    deleteAllPos:
    offs=0
    GET_LIST_SIZE listaPos selected
    IF NOT selected=0
        WHILE NOT offs>selected
            WAIT 0
            GET_LIST_VALUE_BY_INDEX listaPos offs car
            //MARK_OBJECT_AS_NO_LONGER_NEEDED objeto
            DELETE_CAR car
            offs+=1
        ENDWHILE
    ENDIF
    DELETE_LIST listaPos
    RETURN
}







{
    LVAR_INT char
    LVAR_FLOAT x y z
    LVAR_INT mat coord

    testeo:
    mat= char+0x14
    READ_MEMORY mat 4 0 mat
    coord=mat + 0x30
    WRITE_MEMORY coord 4 x 0
    coord+= 0x4
    WRITE_MEMORY coord 4 y 0
    coord+= 0x4
    WRITE_MEMORY coord 4 z 0
    CLEO_RETURN 0
}
{//escribir ID auto
LVAR_INT selected vk_key memory count
LVAR_TEXT_LABEL name test
    GetFormString:
        GET_LABEL_POINTER Coords selected
        WRITE_MEMORY selected 16 0x0 0
        memory = selected
        SET_PLAYER_CONTROL 0 FALSE
        READ_MEMORY 0x00969110 1 0 vk_key  
        WHILE IS_KEY_PRESSED vk_key 
            WAIT 0
        ENDWHILE
        WHILE NOT IS_KEY_PRESSED VK_RETURN // {vk_return} Confirm
        AND NOT (count >= 12)
            WAIT 0
            READ_MEMORY 0x00969110 1 0 vk_key
            IF IS_KEY_PRESSED vk_key
            OR IS_KEY_PRESSED VK_BACK
                IF IS_KEY_PRESSED VK_BACK // {vk_back} Clear
                    WRITE_MEMORY selected 16 0x0 0
                    memory = selected
                    count = 0
                ELSE
                    WRITE_MEMORY memory 1 vk_key 0
                    memory ++
                    count ++
                ENDIF
                WHILE IS_KEY_PRESSED vk_key
                OR IS_KEY_PRESSED VK_BACK
                    WAIT 0 
                ENDWHILE
            ENDIF
            PRINT_FORMATTED_NOW ">~y~%s~w~<" 5200 $selected
        ENDWHILE
        IF SCAN_STRING $selected "%i" count selected
            WAIT 0
        ENDIF
        GET_MODEL_TYPE selected count
        IF count=MODEL_TYPE_VEHICLE
            GET_NAME_OF_VEHICLE_MODEL selected name
            PRINT_FORMATTED_NOW "~b~%s ~w~selected" 1000 $name
            WAIT 1000
        ELSE
            PRINT_FORMATTED_NOW "~r~Model not found or is not a valid vehicle" 1000 $name
            WAIT 1000
            selected=-1
        ENDIF
    CLEO_RETURN 0 selected
}
{
    //weather+
    //gravity
    //wantedlevel++
    //traffic
    //damage vis
    //camera??
    //++
    LVAR_INT weather grav wanted traff dam time offs selected coords mechdam wlfixed timefixed
    LVAR_FLOAT angle
    LVAR_TEXT_LABEL16 idd

raceoptions:
    selected=0
    GET_LABEL_POINTER RaceOpts coords
    READ_MEMORY coords 4 0 weather
    coords+=4
    READ_MEMORY coords 4 0 grav
    coords+=4
    READ_MEMORY coords 4 0 wanted
    coords+=4
    READ_MEMORY coords 4 0 traff
    coords+=4
    READ_MEMORY coords 4 0 dam
    coords+=4
    READ_MEMORY coords 4 0 time
    coords+=4
    READ_MEMORY coords 4 0 wlfixed
    coords+=4
    READ_MEMORY coords 4 0 mechdam
    coords+=4
    READ_MEMORY coords 4 0 timefixed

    GOSUB updateStrings

    SET_TEXT_DRAW_BEFORE_FADE 1
    USE_TEXT_COMMANDS 1
    coords=0
    WHILE TRUE
        WAIT 0
    //y+=20 o 35 si es diferente

        DRAW_RECT 115.0 30.0 170.0 30.0 0 25 70 200
        
        DRAW_RECT 115.0 190.0 170.0 290.0 0 0 0 200

        DRAW_RECT 115.0 45.0 170.0 5.0 180 180 180 255
        

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.40 1.61
        SET_TEXT_COLOUR 230 230 230 255
        DISPLAY_TEXT 85.0 22.0 RCFOPTI

    //time
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
        IF selected=0
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 60.0 RCS-ROT

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.35 1.56
        IF selected=0
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF
        DISPLAY_TEXT 90.0 61.0 RC+TIMW //sunrise, etc

    //fixed time
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6 //0.39 1.6
        IF selected=1
            PRINT_FORMATTED_NOW "Sets if the time will be fixed or not (if yes, the time will not change)" 50
            GET_LABEL_POINTER RaceOpts coords
            coords+=32
            READ_MEMORY coords 4 0 offs
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 85.0 RCS--FI

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.45 1.66 //0.39 1.6
        IF selected=1
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF
        DISPLAY_TEXT 90.0 85.0 RC+TIEF

    //weather
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=4
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 190.0 RCS--WE

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.35 1.56
        IF selected=4
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF
        DISPLAY_TEXT 120.0 191.0 RC+WEAW

    


    //wanted level
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=2
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 120.0 RCS--WL

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.35 1.56
        IF selected=2
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF
        DISPLAY_TEXT 135.0 120.0 RC+WANW

    //fixed wantedlvl
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=3
            PRINT_FORMATTED_NOW "Sets if the wanted level will be fixed or not (if yes, it will not change during the race)" 50
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 145.0 RCS--FI

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=3
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF
        DISPLAY_TEXT 90.0 145.0 RC+WLFX

    //mech damage
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        SET_TEXT_BACKGROUND 1
        IF selected=5
            PRINT_FORMATTED_NOW "Sets if your car will be be damaged or not" 50
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        DISPLAY_TEXT 50.0 215.0 RCS-RWD

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=5
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF
        DISPLAY_TEXT 120.0 215.0 RC+WWEF
    //damage
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=6
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 240.0 RCS-ROV
        
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=6
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF

        DISPLAY_TEXT 160.0 240.0 RC+DAVM

    //Traffic
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=7
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 265.0 RCS-RTR
        
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=7
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF

        DISPLAY_TEXT 110.0 265.0 RC+TRFF

    //Gravity
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=8
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 290.0 RCS--GR
        
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6
        IF selected=8
            SET_TEXT_COLOUR 0 150 220 240
        ELSE
            SET_TEXT_COLOUR 0 150 220 50
        ENDIF

        DISPLAY_TEXT 112.0 290.0 RC+GR-Y
    // 
    
        IF IS_KEY_JUST_PRESSED VK_DOWN
        OR IS_KEY_JUST_PRESSED VK_RIGHT
        OR IS_KEY_JUST_PRESSED VK_KEY_S
                IF selected=8
                    selected=0
                ELSE
                    selected+=1
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_UP
        OR IS_KEY_JUST_PRESSED VK_LEFT
        OR IS_KEY_JUST_PRESSED VK_KEY_W
                IF selected=0
                    selected=8
                ELSE
                    selected-=1
                ENDIF
                GET_AUDIO_SFX_VOLUME angle
                //WRITE_MEMORY coords 4 selected 0
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_SPACE
            GET_AUDIO_SFX_VOLUME angle
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 (1.0 angle 1) ()
            SWITCH selected
                CASE 0
                    IF NOT time=4
                        time+=1
                    ELSE
                        time=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 1
                    IF timefixed=0
                        timefixed=1
                    ELSE
                        timefixed=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 2
                    IF NOT wanted=7
                        wanted+=1
                    ELSE
                        wanted=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 3
                    IF wlfixed=0
                        wlfixed=1
                    ELSE
                        wlfixed=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 4
                    IF NOT weather=6
                        weather+=1
                    ELSE
                        weather=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 5
                    IF mechdam=0
                        mechdam=1
                    ELSE
                        mechdam=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 6
                    IF dam=0
                        dam=1
                    ELSE
                        dam=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 7
                    IF NOT traff=3
                        traff+=1
                    ELSE
                        traff=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
                CASE 8
                    IF NOT grav=4
                        grav+=1
                    ELSE
                        grav=0
                    ENDIF
                    GOSUB updateStrings
                    BREAK
            ENDSWITCH
        ENDIF



        IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE
            WHILE IS_BUTTON_PRESSED PAD1 TRIANGLE
                WAIT 0
            ENDWHILE
            GET_LABEL_POINTER RaceOpts coords

            WRITE_MEMORY coords 4 weather 0
            coords+=4
            WRITE_MEMORY coords 4 grav 0
            coords+=4
            WRITE_MEMORY coords 4 wanted 0
            coords+=4
            WRITE_MEMORY coords 4 traff 0
            coords+=4
            WRITE_MEMORY coords 4 dam 0
            coords+=4
            WRITE_MEMORY coords 4 time 0
            coords+=4
            WRITE_MEMORY coords 4 wlfixed 0
            coords+=4
            WRITE_MEMORY coords 4 mechdam 0
            coords+=4
            WRITE_MEMORY coords 4 timefixed 0
            CLEO_RETURN 0
        ENDIF
    ENDWHILE

    //
    updateStrings:
        IF dam=1
            ADD_TEXT_LABEL RC+DAVM "On"
        ELSE
            ADD_TEXT_LABEL RC+DAVM "Off"
        ENDIF

        IF mechdam=1
            ADD_TEXT_LABEL RC+WWEF "On"
        ELSE
            ADD_TEXT_LABEL RC+WWEF "Off"
        ENDIF

        IF timefixed=1
            ADD_TEXT_LABEL RC+TIEF "On"
        ELSE
            ADD_TEXT_LABEL RC+TIEF "Off"
        ENDIF

        IF wlfixed=1
            ADD_TEXT_LABEL RC+WLFX "On"
        ELSE
            ADD_TEXT_LABEL RC+WLFX "Off"
        ENDIF

        SWITCH time
            CASE 0
                ADD_TEXT_LABEL RC+TIMW "Default"
                BREAK
            CASE 1
                ADD_TEXT_LABEL RC+TIMW "Sunrise"
                BREAK
            CASE 2
                ADD_TEXT_LABEL RC+TIMW "Midday"
                BREAK
            CASE 3
                ADD_TEXT_LABEL RC+TIMW "Afternoon"
                BREAK
            CASE 4
                ADD_TEXT_LABEL RC+TIMW "Night"
                BREAK
        ENDSWITCH

        SWITCH weather
            CASE 0
                ADD_TEXT_LABEL RC+WEAW "Default"
                BREAK
            CASE 1
                ADD_TEXT_LABEL RC+WEAW "Sunny"
                BREAK
            CASE 2
                ADD_TEXT_LABEL RC+WEAW "Cloudy"
                BREAK
            CASE 3
                ADD_TEXT_LABEL RC+WEAW "Rainy"
                BREAK
            CASE 4
                ADD_TEXT_LABEL RC+WEAW "Storm"
                BREAK
            CASE 5
                ADD_TEXT_LABEL RC+WEAW "Foggy"
                BREAK
            CASE 6
                ADD_TEXT_LABEL RC+WEAW "Sandstorm"
                BREAK   
        ENDSWITCH
        IF NOT wanted=0
        AND NOT wanted=7
            STRING_FORMAT idd "%i_Stars" wanted
        ELSE
            IF wanted=0
                idd="Off"
            ELSE
                idd="Default"
            ENDIF
        ENDIF
        ADD_TEXT_LABEL RC+WANW $idd
        SWITCH traff
            CASE 0
                ADD_TEXT_LABEL RC+TRFF "Off"
                BREAK
            CASE 1
                ADD_TEXT_LABEL RC+TRFF "Low"
                BREAK
            CASE 2
                ADD_TEXT_LABEL RC+TRFF "Medium"
                BREAK
            CASE 3
                ADD_TEXT_LABEL RC+TRFF "High"
                BREAK
        ENDSWITCH
        SWITCH grav
            CASE 0
                ADD_TEXT_LABEL RC+GR-Y "Default"
                BREAK
            CASE 1
                ADD_TEXT_LABEL RC+GR-Y "Very_low"
                BREAK
            CASE 2
                ADD_TEXT_LABEL RC+GR-Y "Low"
                BREAK
            CASE 3
                ADD_TEXT_LABEL RC+GR-Y "High"
                BREAK
            CASE 4
                ADD_TEXT_LABEL RC+GR-Y "Very_high"
                BREAK
        ENDSWITCH
        RETURN
    //

}

{
    LVAR_INT scplayer
    LVAR_INT pPath         // puntero al buffer del path
    LVAR_INT slotObj       // ECX = objeto del slot TM
    LVAR_INT tmCar         // handle CLEO del auto spawneado
    LVAR_INT keyPressed    // anti-repeticion de tecla
    LVAR_INT tmLoadFunc    // VA de la funcion de load del TM
    LVAR_INT tmBase        // base del modulo TM.asi en memoria

cargarTmv:
    // Configuracion
    // Cambia este string por el path completo a tu .tmv
    // El TM guarda los archivos en: GTA San Andreas\CLEO\Tuning Mod\
    CONST_STRING TMV_FILE "CLEO\Tuning Mod\Vehicles\barracuda.tmv"
 
    // VA fija de la funcion de load (image base 0x10000000)
    CONST_INT TM_LOAD_FUNC  0x10005E70
 
    // Slot object 0 (ECX para __thiscall)
    CONST_INT TM_SLOT0_OBJ  0x10041CAC
 
    GET_PLAYER_CHAR 0 scplayer
    keyPressed = 0
 
    WHILE TRUE
        WAIT 0
 
        // Detectar H (solo al presionar, no al mantener)
        IF IS_KEY_PRESSED KEY_H
            IF keyPressed = 0
                keyPressed = 1
                GOSUB SpawnTMCar
            ENDIF
        ELSE
            keyPressed = 0
        ENDIF
 
    ENDWHILE
}

{
LVAR_INT car testcar i selected vueltapl ich offs pos totlap ooo num size coords tipo char filename almacen
LVAR_FLOAT x y z x2 y2 z2 distance xtc ytc ztc distanceplay
LVAR_TEXT_LABEL idd
posCalc:
    STREAM_CUSTOM_SCRIPT_FROM_LABEL checkpos car
    CLEO_RETURN 0
checkpos:
    WAIT 0
    //GET_LIST_SIZE listaOpp size
    num=1

    GET_LABEL_POINTER Helper coords
    coords+=4
    READ_MEMORY coords 4 0 size
    IF num=3829
        GET_RANDOM_CAR_IN_SPHERE_NO_SAVE x y z 89.0 0 testcar
    ENDIF

    GET_LABEL_POINTER Auxiliar ich
    ich+=12
    WRITE_MEMORY ich 4 0 0

    WHILE TRUE
        WAIT 0
        
        GET_LABEL_POINTER Opponents coords
        ooo = num*4
        coords+=ooo
        READ_MEMORY coords 4 0 testcar

        ooo=0
        GET_LABEL_POINTER Opponentslap ich
        READ_MEMORY ich 4 0 vueltapl
        GET_EXTENDED_CAR_VAR testcar "RaceC++" 10 selected
        IF vueltapl=-1
            GET_LABEL_POINTER Totlaps ich
            READ_MEMORY ich 4 0 totlap
            vueltapl=totlap
            ooo=1
        ENDIF
        IF selected=-2
        OR selected=-1
            GET_LABEL_POINTER Totlaps ich
            READ_MEMORY ich 4 0 totlap
            selected=totlap
        ENDIF

        GET_EXTENDED_CAR_VAR testcar "RaceC++" 11 offs
        IF offs=0
            selected-=1
        ENDIF

        // PRINT_FORMATTED_NOW "%i %i" 1000 vueltapl selected
        IF vueltapl=selected
            GET_LABEL_POINTER IsCircuit ich
            READ_MEMORY ich 4 0 offs
            GET_EXTENDED_CAR_VAR testcar "RaceC++" 11 selected
            IF offs=0
                GET_LABEL_POINTER Auxiliar ich
                ich+=8
                READ_MEMORY ich 4 0 offs
                IF offs=selected
                    selected=-2
                ENDIF
            ENDIF
            IF NOT selected=-1

                selected+=1
                //-2 opp yendo a ultimo
                //0 yendo pl a ultimo ch
                //1 yendo pl a ch 0
                

                IF selected=1
                    GET_LABEL_POINTER Auxiliar ich
                    ich+=8
                    READ_MEMORY ich 4 0 offs
                    selected=offs+2
                ENDIF


                IF selected=-1
                OR selected=0
                    GET_LABEL_POINTER Auxiliar ich
                    ich+=8
                    READ_MEMORY ich 4 0 offs
                    selected=offs+3
                ENDIF

                GET_LABEL_POINTER Opponentschar coords
                coords+=4
                READ_MEMORY coords 4 0 i

                                
                GET_LABEL_POINTER IsCircuit ich
                READ_MEMORY ich 4 0 offs
                IF offs=0
                    IF i=0
                    OR i=-11
                        GET_LABEL_POINTER Auxiliar ich
                        ich+=8
                        READ_MEMORY ich 4 0 offs
                        GET_LABEL_POINTER Auxiliar ich
                        ich+=12
                        READ_MEMORY ich 4 0 coords
                        IF coords=0
                            i=2
                        ELSE
                            i=offs+3
                        ENDIF
                    ENDIF
                    IF i=3
                        GET_LABEL_POINTER Auxiliar ich
                        ich+=12
                        WRITE_MEMORY ich 4 1 0
                    ENDIF
                ELSE

                    IF i=0
                    OR i=1
                    OR i=-1
                    OR i=-1
                        GET_LABEL_POINTER Auxiliar ich
                        ich+=8
                        READ_MEMORY ich 4 0 offs
                        IF i=0
                            IF vueltapl=1
                                GET_LABEL_POINTER Auxiliar ich
                                ich+=12
                                READ_MEMORY ich 4 0 coords //si ya paso una vez por el ch 0 en vuelta 0
                                IF coords=0
                                    i=2
                                ELSE
                                    i=offs+0
                                    IF ooo=1
                                        i=offs+3 
                                    ENDIF
                                ENDIF      
                            ELSE
                                i=offs+0
                                IF ooo=1
                                    i=offs+3 
                                ENDIF
                            ENDIF 
                        ELSE
                            IF i=-1
                            OR i=-11
                                i=offs                   
                            ELSE
                                i=offs+2
                                GET_LABEL_POINTER Auxiliar ich
                                ich+=12
                                READ_MEMORY ich 4 0 offs

                                IF offs=0
                                    WRITE_MEMORY ich 4 1 0
                                ENDIF
                            ENDIF

                        ENDIF
                    ENDIF
                ENDIF
                GET_EXTENDED_CAR_VAR testcar "RaceC++" 12 offs
          
                IF offs=0
                    almacen=1
                    WHILE almacen=1
                        WAIT 0
                        GET_LABEL_POINTER Coords2 coords
                        STRING_FORMAT idd "%i" selected
                        GET_LABEL_POINTER Dumper filename
                        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $idd coords
                        SCAN_STRING $coords "%f %f %f %f %i %f %i" offs x y z xtc char ytc tipo
                        IF NOT tipo=0
                            almacen=0
                            selected+=1
                        ELSE
                            selected+=1
                        ENDIF
                    ENDWHILE
                ELSE
                    almacen=10
                ENDIF
//
                //PRINT_FORMATTED_NOW "%i %i %i" 1000 i selected vueltapl
                IF i=selected
                    GET_CAR_COORDINATES testcar x2 y2 z2
                    IF almacen=10
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 1 xtc
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 2 ytc
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 3 ztc
                    ELSE
                        STRING_FORMAT idd "%i" selected
                        GET_LABEL_POINTER Dumper filename
                        GET_LABEL_POINTER Coords2 coords
                        READ_STRING_FROM_INI_FILE $filename "Checkpoints" $idd coords
                        SCAN_STRING $coords "%f %f %f" offs xtc ytc ztc
                    ENDIF
                    //GET_LABEL_POINTER Coords distance
                    GET_DISTANCE_BETWEEN_COORDS_3D x2 y2 z2 xtc ytc ztc distance
                    GET_CAR_COORDINATES car x y z
                    GET_DISTANCE_BETWEEN_COORDS_3D x y z xtc ytc ztc distanceplay
                    IF distance<distanceplay
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 14 ich
                        IF NOT ich=1
                            SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 1
                            GET_LABEL_POINTER Pos selected
                            READ_MEMORY selected 4 0 ich
                            ich+=1
                            pos=ich
                            WRITE_MEMORY selected 4 ich 0
                            //USE_TEXT_COMMANDS 1
                            //USE_TEXT_COMMANDS 0
                            //GOSUB showpos
                        ENDIF
                    ELSE
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 14 ich
                        IF NOT ich=0
                            SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 0
                            GET_LABEL_POINTER Pos selected
                            READ_MEMORY selected 4 0 ich
                            IF NOT ich=1
                                ich-=1
                            ENDIF
                            pos=ich
                            WRITE_MEMORY selected 4 ich 0
                            //USE_TEXT_COMMANDS 1
                            //USE_TEXT_COMMANDS 0
                            //GOSUB showpos
                        ENDIF
                    ENDIF
                ELSE
                    IF selected<i
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 14 ich
                        IF NOT ich=0
                            SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 0
                            GET_LABEL_POINTER Pos selected
                            READ_MEMORY selected 4 0 ich
                            IF NOT ich=1
                                ich-=1
                            ENDIF
                            pos=ich
                            WRITE_MEMORY selected 4 ich 0
                            //USE_TEXT_COMMANDS 1
                            //USE_TEXT_COMMANDS 0
                            //GOSUB showpos
                        ENDIF
                    ELSE
                        GET_EXTENDED_CAR_VAR testcar "RaceC++" 14 ich
                        IF NOT ich=1
                            SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 1
                            GET_LABEL_POINTER Pos selected
                            READ_MEMORY selected 4 0 ich
                            ich+=1
                            pos=ich
                            WRITE_MEMORY selected 4 ich 0
                            //USE_TEXT_COMMANDS 1
                            //USE_TEXT_COMMANDS 0
                            //GOSUB showpos
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ELSE
            IF selected<vueltapl
                GET_EXTENDED_CAR_VAR testcar "RaceC++" 14 ich
                IF NOT ich=0
                    SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 0
                    GET_LABEL_POINTER Pos selected
                    READ_MEMORY selected 4 0 ich
                    IF NOT ich=1
                        ich-=1
                    ENDIF
                    pos=ich
                    WRITE_MEMORY selected 4 ich 0
                    //USE_TEXT_COMMANDS 1
                    //USE_TEXT_COMMANDS 0
                    //GOSUB showpos
                ENDIF
            ELSE
                GET_EXTENDED_CAR_VAR testcar "RaceC++" 14 ich
                IF NOT ich=1
                    SET_EXTENDED_CAR_VAR testcar "RaceC++" 14 1
                    GET_LABEL_POINTER Pos selected
                    READ_MEMORY selected 4 0 ich
                    ich+=1
                    pos=ich
                    WRITE_MEMORY selected 4 ich 0
                    //USE_TEXT_COMMANDS 1
                    //USE_TEXT_COMMANDS 0
                    //GOSUB showpos
                ENDIF
            ENDIF
        ENDIF

        GET_LABEL_POINTER Helper coords
        READ_MEMORY coords 4 0 offs
        IF offs=5
            SET_TEXT_DRAW_BEFORE_FADE 0
            GET_LABEL_POINTER Auxiliar ich
            ich+=12
            WRITE_MEMORY ich 4 0 0
            TERMINATE_THIS_CUSTOM_SCRIPT
        ENDIF

        num+=1
        IF num=size
            num=1
        ENDIF
    ENDWHILE
}
{
    LVAR_INT an mes dia dia2 hora min seg mil res
    getRandomSeed:
        GET_LOCAL_TIME an mes dia dia2 hora min seg mil
        res=dia2+hora
        res+=min
        res+=seg
        res+=mil
        CLEO_RETURN 0 res
}

{

LVAR_INT lista filename objeto offs scplayer id menu selected car simetrico
LVAR_FLOAT x y z scale camx camy x2 y2 z2 cos sen seny zoom axis angle
LVAR_TEXT_LABEL16 idd
//angle=xrot, axis=yrot, x2=angle
menuobjeto:
    selected=0
    USE_TEXT_COMMANDS 1
    SET_TEXT_DRAW_BEFORE_FADE 1
    WHILE TRUE
        WAIT 0
    //     DRAW_RECT 115.0 30.0 170.0 30.0 231 85 21 200
        
    //     DRAW_RECT 115.0 115.0 170.0 150.0 0 0 0 200

    //     DRAW_RECT 115.0 45.0 170.0 5.0 180 180 180 255

    //     SET_TEXT_FONT 1
    //     SET_TEXT_EDGE 1 0 0 0 255
    //     SET_TEXT_SCALE 0.40 1.61
    //     SET_TEXT_COLOUR 230 230 230 255
    //     DISPLAY_TEXT 85.0 22.0 RCF-OBW
    //   //add
    //     SET_TEXT_FONT 1
    //     SET_TEXT_EDGE 1 0 0 0 255
    //     SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
    //     IF selected=0
    //         SET_TEXT_COLOUR 230 230 150 240
    //     ELSE
    //         SET_TEXT_COLOUR 230 230 230 50
    //     ENDIF
    //     SET_TEXT_BACKGROUND 1
    //     DISPLAY_TEXT 50.0 60.0 RCO-+AD
    //   //edit
    //     SET_TEXT_FONT 1
    //     SET_TEXT_EDGE 1 0 0 0 255
    //     SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
    //     IF selected=1
    //         SET_TEXT_COLOUR 230 230 150 240
    //         PRINT_FORMATTED_NOW "Change the scale/position of already placed objects or remove them." 100
    //     ELSE
    //         SET_TEXT_COLOUR 230 230 230 50
    //     ENDIF
    //     SET_TEXT_BACKGROUND 1
    //     DISPLAY_TEXT 50.0 85.0 RCO--ED
        
    //   //exit

    //     SET_TEXT_FONT 1
    //     SET_TEXT_EDGE 1 0 0 0 255
    //     SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
    //     IF selected=2
    //         SET_TEXT_COLOUR 230 230 150 240
    //     ELSE
    //         SET_TEXT_COLOUR 230 230 230 50
    //     ENDIF
    //     SET_TEXT_BACKGROUND 1
    //     DISPLAY_TEXT 50.0 110.0 RCFEXIT


            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 190.0 120.0 140.0 180.0 0.0 0 0 0 0 0 0 200

            DRAW_STRING_EXT "Objects" DRAW_EVENT_BEFORE_HUD 80.0 115.0 0.5 1.0 0 0 1 0 120.0 1 255 255 255 255 1 1 0 0 0 200 0 255 79 28 200
            DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 80.0 125.0 120.0 30.0 180.0 0.0 0 0 0 120 120 120 220
            DRAW_RECT 80.0 110.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 140.0 120.0 3.0 50 50 50 255
            DRAW_RECT 80.0 260.0 120.0 3.0 50 50 50 255
            SWITCH selected
                CASE 0
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 220.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    BREAK
                CASE 1
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 170 90 90 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 220.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 100 0 0 200
                    PRINT_FORMATTED_NOW "Change the scale/position of already placed objects or remove them." 100
                    BREAK
                CASE 2
                    DRAW_STRING_EXT "Add" DRAW_EVENT_BEFORE_HUD 80.0 160.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Edit" DRAW_EVENT_BEFORE_HUD 80.0 190.1 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 0 0 0 200
                    DRAW_STRING_EXT "Exit" DRAW_EVENT_BEFORE_HUD 80.0 220.0 0.3 0.8 0 2 1 0 100.0 1 255 255 255 255 1 1 0 0 0 200 1 160 30 30 200
                    BREAK
            ENDSWITCH
            
      //controls
        IF IS_KEY_JUST_PRESSED VK_KEY_Z
            CLEO_RETURN 0
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_DOWN
        OR IS_KEY_JUST_PRESSED VK_RIGHT
        OR IS_KEY_JUST_PRESSED VK_KEY_S
            IF selected=2
                selected=0
            ELSE
                selected+=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_UP
        OR IS_KEY_JUST_PRESSED VK_LEFT
        OR IS_KEY_JUST_PRESSED VK_KEY_W
            IF selected=0
                selected=2
            ELSE
                selected-=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF
      //switch
        IF IS_KEY_JUST_PRESSED VK_SPACE
        OR IS_KEY_JUST_PRESSED VK_NUMPAD5
            SWITCH selected
                CASE 0
                    GOTO objectscr
                    BREAK
                CASE 1
                    GET_LABEL_POINTER Coords offs
                    IF READ_STRING_FROM_INI_FILE $filename "Objects" "1" offs
                    AND NOT IS_STRING_EQUAL $offs "DELETED" 10 0 "z"
                        GOTO objectsedit
                    ELSE
                        PRINT_FORMATTED_NOW "~r~There are not objects!" 100
                    ENDIF
                    BREAK
                CASE 2
                    USE_TEXT_COMMANDS 0
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    CLEO_RETURN 0
                    BREAK
            ENDSWITCH
        ENDIF
        
        IF IS_BUTTON_JUST_PRESSED PAD1 TRIANGLE   
            USE_TEXT_COMMANDS 0
            SET_TEXT_DRAW_BEFORE_FADE 0
            CLEO_RETURN 0
        ENDIF

    ENDWHILE
CLEO_RETURN 0
prepplayer:
    GET_PLAYER_CHAR 0 scplayer
    GET_LABEL_POINTER PlayerCoords2 offs
    GET_CHAR_COORDINATES scplayer x y z
    STRING_FORMAT offs "%f %f %f" x y z
    GET_LABEL_POINTER MenusHelp offs
    WRITE_MEMORY offs 4 1 0
    FREEZE_CHAR_POSITION scplayer 1
    SET_CHAR_COLLISION scplayer 0
    SET_CHAR_VISIBLE scplayer 0
    IF IS_CHAR_IN_ANY_CAR scplayer
    OR IS_CHAR_IN_ANY_BOAT scplayer
    OR IS_CHAR_IN_FLYING_VEHICLE scplayer
        GET_CAR_CHAR_IS_USING scplayer car
        SET_CAR_VISIBLE car 0
        //FREEZE_CAR_POSITION_AND_DONT_LOAD_COLLISION car 1
        SET_CAR_COLLISION car 0
        FREEZE_CAR_POSITION car 1
    ENDIF

RETURN
objectscr:
    GOSUB prepplayer
    axis=0.0
    angle=0.0
    zoom=10.0
    scale=1.0
    simetrico=0
    //SET_PLAYER_CONTROL 0 1
    //SET_PLAYER_CONTROL_PAD_MOVEMENT 0 0
    
    IF NOT READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Objects" "1" id
        id=1633
    ENDIF
    x+=10.0
    y+=10.0
    CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
    

    SET_OBJECT_PROOFS objeto 1 1 1 1 1
    SET_OBJECT_COLLISION objeto 0
    GET_OBJECT_COORDINATES objeto x y z

    x2=0.0
    SET_OBJECT_ROTATION objeto angle axis x2

    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS objeto 10.0 5.0 5.0 x y z
    //SET_CAMERA_CONTROL 1
    GET_PC_MOUSE_MOVEMENT camx camy
    SET_FIXED_CAMERA_POSITION x y z camx camy 10.0
    GET_OBJECT_COORDINATES objeto x y z
    POINT_CAMERA_AT_POINT x y z 2
    USE_TEXT_COMMANDS 1
    SET_TEXT_DRAW_BEFORE_FADE 1
    idd="im_an_easteregg"
    WHILE TRUE

        WAIT 0

        DRAW_STRING "Press ~y~Z~w~ to open the menu" DRAW_EVENT_BEFORE_HUD 500.0 380.0 0.3 0.7 1 1
        DRAW_STRING "Press ~y~B~w~ to show help" DRAW_EVENT_BEFORE_HUD 500.0 395.0 0.3 0.7 1 1
        DRAW_STRING "Press ~y~I~w~ and ~y~O~w~ to change object" DRAW_EVENT_BEFORE_HUD 500.0 410.0 0.3 0.7 1 1

        IF simetrico=1
            DRAW_STRING "Press ~y~SHIFT~w~ + ~y~T~w~ to ~r~disable~w~ acuratte mode" DRAW_EVENT_BEFORE_HUD 500.0 425.0 0.3 0.7 1 1
        ELSE
            DRAW_STRING "Press ~y~SHIFT~w~ + ~y~T~w~ to ~g~enable~w~ acuratte mode" DRAW_EVENT_BEFORE_HUD 500.0 425.0 0.3 0.7 1 1
        ENDIF

        GET_LABEL_POINTER Coords offs
        GET_OBJECT_COORDINATES objeto x y z

        STRING_FORMAT offs "~b~X~w~=%f" x
        DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 280.0 0.3 0.7 1 1
        STRING_FORMAT offs "~b~Y~w~=%f" y
        DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 295.0 0.3 0.7 1 1
        STRING_FORMAT offs "~b~Z~w~=%f" z
        DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 310.0 0.3 0.7 1 1
        STRING_FORMAT offs "~b~Scale~w~=%f" scale
        DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 325.0 0.3 0.7 1 1

        GET_OBJECT_MODEL objeto offs
        STRING_FORMAT idd "~b~ID~w~=%i" offs
        DRAW_STRING $idd DRAW_EVENT_BEFORE_HUD 60.0 265.0 0.3 0.7 1 1

        GET_ACTIVE_CAMERA_COORDINATES x2 y2 z2
        SET_CHAR_COORDINATES scplayer x2 y2 z2
        GET_PC_MOUSE_MOVEMENT x2 y2
        IF x2>10.0
            x2=10.0
        ENDIF
        IF y2>10.0
            y2=10.0
        ENDIF
        
        
        camx+=x2
        camy+=y2

        IF camx>360.0
            camx=0.0
        ENDIF
        IF camx<0.0
            camx=360.0
        ENDIF

        IF camy>90.0
            camy=90.0
        ENDIF
        IF camy<-90.0
            camy=-90.0
        ENDIF
        
        

        SIN camx sen
        COS camx cos
        sen*=zoom
        cos*=zoom

        //PRINT_FORMATTED_NOW "y=%f x=%f sen=%f cos=%f" 100 camy camx sen cos

        SIN camy seny
        seny-=0.0
        seny*=zoom
        //GET_TARGET_BLIP_COORDS x y z
        //PRINT_FORMATTED_NOW "%f %f %f" 100 x y z
        GET_OBJECT_COORDINATES objeto x y z
        x+=sen
        y+=cos
        z+=seny
        
        SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
       
        GET_OBJECT_COORDINATES objeto x y z
        POINT_CAMERA_AT_POINT x y z 2

        IF IS_KEY_PRESSED VK_LSHIFT
        AND IS_KEY_JUST_PRESSED VK_KEY_T
            IF simetrico=0
                simetrico=1
            ELSE
                simetrico=0
            ENDIF
        ENDIF
        IF IS_MOUSE_WHEEL_UP
            zoom-=@0.5
        ENDIF
        IF IS_MOUSE_WHEEL_DOWN
            zoom+=@0.5
        ENDIF
        
        IF IS_KEY_JUST_PRESSED VK_KEY_B
            GOSUB showhelp
        ENDIF

        IF simetrico=0
            IF IS_KEY_PRESSED VK_KEY_P
                
                GET_OBJECT_COORDINATES objeto x y z

                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    z+=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        z+=@0.5
                    ELSE
                        z+=@0.1
                    ENDIF
                ENDIF
                
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_L
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    z-=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        z-=@0.5
                    ELSE
                        z-=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
            ENDIF
            
            IF IS_KEY_PRESSED VK_KEY_D
            OR IS_KEY_PRESSED VK_RIGHT
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x+=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        x+=@0.5
                    ELSE
                        x+=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_A
            OR IS_KEY_PRESSED VK_LEFT
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x-=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        x-=@0.5
                    ELSE
                        x-=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_W
            OR IS_KEY_PRESSED VK_UP
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    y+=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        y+=@0.5
                    ELSE
                        y+=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_S
            OR IS_KEY_PRESSED VK_DOWN
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    y-=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        y-=@0.5
                    ELSE
                        y-=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
            ENDIF

            IF IS_KEY_PRESSED VK_KEY_M
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    scale+=@0.005
                ELSE
                    IF IS_KEY_PRESSED 16
                        scale+=@0.1
                    ELSE
                        scale+=@0.01
                    ENDIF 
                ENDIF
                SET_OBJECT_SCALE objeto scale

            ENDIF
            IF IS_KEY_PRESSED VK_KEY_N
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    scale-=@0.005
                ELSE
                    IF IS_KEY_PRESSED 16
                        scale-=@0.1
                    ELSE
                        scale-=@0.01
                    ENDIF 
                ENDIF
                SET_OBJECT_SCALE objeto scale
            ENDIF

            IF IS_KEY_PRESSED VK_KEY_E 
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x2+=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        x2+=@1.0
                    ELSE
                        x2+=@0.5
                    ENDIF 
                ENDIF

                SET_OBJECT_ROTATION objeto angle axis x2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_Q 
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x2-=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        x2-=@1.0
                    ELSE
                        x2-=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
            ENDIF

            IF IS_KEY_PRESSED VK_KEY_J
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    axis+=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        axis+=@1.0
                    ELSE
                        axis+=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_G
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    axis-=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        axis-=@1.0
                    ELSE
                        axis-=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
            ENDIF

            IF IS_KEY_PRESSED VK_KEY_Y
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    angle+=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        angle+=@1.0
                    ELSE
                        angle+=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
            ENDIF
            IF IS_KEY_PRESSED VK_KEY_H
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    angle-=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        angle-=@1.0
                    ELSE
                        angle-=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2 //angle=xrot, axis=yrot, x2=angle
            ENDIF
        ELSE
            GOSUB simetricomodo
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_SPACE
            SET_OBJECT_COLLISION objeto 1
            GET_OBJECT_COORDINATES objeto x y z
            GET_OBJECT_HEADING objeto x2
            LIST_ADD lista objeto
            GOSUB saveObjIni
            CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
            SET_OBJECT_COLLISION objeto 0
            SET_OBJECT_PROOFS objeto 1 1 1 1 1
            SET_OBJECT_ROTATION objeto angle axis x2
            SET_OBJECT_SCALE objeto scale
        ENDIF

        /*IF IS_KEY_JUST_PRESSED VK_KEY_X
            CLEO_CALL GetObjString 0 id
            GET_MODEL_TYPE id offs
            IF NOT offs=MODEL_TYPE_VEHICLE
            AND NOT offs=MODEL_TYPE_INVALID
            AND NOT offs=MODEL_TYPE_PED
            AND NOT offs=MODEL_TYPE_WEAPON
                GET_OBJECT_HEADING objeto x2
                DELETE_OBJECT objeto
                CREATE_OBJECT_NO_OFFSET id x y z objeto
                SET_OBJECT_COLLISION objeto 0
                SET_OBJECT_ROTATION objeto angle axis x2
                SET_OBJECT_SCALE objeto scale
                PRINT_FORMATTED_NOW "~b~%i~w~ loaded!" 1000 id
            ELSE
                PRINT_FORMATTED_NOW "~y~%i~r~ is not a valid object ID!" 1000 id
            ENDIF
        ENDIF*/

        IF IS_KEY_JUST_PRESSED VK_BACK
            GET_OBJECT_HEADING objeto x2
            DELETE_OBJECT objeto
            GET_LIST_SIZE lista offs
            offs-=1
            GET_LIST_VALUE_BY_INDEX lista offs objeto
            DELETE_OBJECT objeto
            PRINT_FORMATTED_NOW "Latest placed object deleted." 500
            LIST_REMOVE_INDEX lista offs
            offs+=1
            STRING_FORMAT idd "%i" offs
            GET_LABEL_POINTER Coords offs
            STRING_FORMAT offs "DELETED"
            WRITE_STRING_TO_INI_FILE $offs $filename "Objects" $idd
            CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
            SET_OBJECT_COLLISION objeto 0
            SET_OBJECT_PROOFS objeto 1 1 1 1 1
            SET_OBJECT_ROTATION objeto angle axis x2
            SET_OBJECT_SCALE objeto scale
        ENDIF
       
        IF IS_KEY_JUST_PRESSED VK_KEY_Z
            GOSUB menuobj
            IF IS_STRING_EQUAL $idd "CLOSE_NOW" 12 0 "p"
                GOTO stopobj
            ENDIF
        ENDIF
        
        IF IS_KEY_JUST_PRESSED VK_KEY_O
            GET_LABEL_POINTER MenusHelp offs
            READ_MEMORY offs 4 0 id
            id+=1
            WRITE_MEMORY offs 4 id 0
            STRING_FORMAT idd "%i" id
            IF NOT READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Objects" $idd id
                GET_LABEL_POINTER MenusHelp offs
                WRITE_MEMORY offs 4 1 0
                IF NOT READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Objects" "1" id
                    id=1633
                ENDIF
            ENDIF
            
            GET_OBJECT_COORDINATES objeto x y z
            GET_OBJECT_HEADING objeto x2
            DELETE_OBJECT objeto
            CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
            SET_OBJECT_COLLISION objeto 0
            SET_OBJECT_SCALE objeto scale

            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_KEY_I
            GET_LABEL_POINTER MenusHelp offs
            READ_MEMORY offs 4 0 id
            id-=1
            WRITE_MEMORY offs 4 id 0
            STRING_FORMAT idd "%i" id
            IF NOT READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Objects" $idd id
                id=1
                STRING_FORMAT idd "%i" id
                WHILE READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Objects" $idd selected
                    id+=1
                    STRING_FORMAT idd "%i" id
                ENDWHILE
                id-=1
                WRITE_MEMORY offs 4 id 0
                STRING_FORMAT idd "%i" id
                READ_INT_FROM_INI_FILE "CLEO/Race creator++/General settings.ini" "Objects" $idd id
            ENDIF
            
            GET_OBJECT_COORDINATES objeto x y z
            GET_OBJECT_HEADING objeto x2
            DELETE_OBJECT objeto
            CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
            SET_OBJECT_COLLISION objeto 0
            SET_OBJECT_SCALE objeto scale

            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF
        /*IF IS_KEY_JUST_PRESSED VK_KEY_C
            GOSUB menuobj
            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF*/
    ENDWHILE
    CLEO_RETURN 0

simetricomodo:
    IF IS_KEY_JUST_PRESSED VK_KEY_P        
        GET_OBJECT_COORDINATES objeto x y z

        IF IS_KEY_PRESSED 17 //17 control, 16 shift
            z+=@0.01
        ELSE
            IF IS_KEY_PRESSED 16
                z+=@0.5
            ELSE
                z+=@0.1
            ENDIF
        ENDIF

        SET_OBJECT_COORDINATES objeto x y z
        x+=sen
        y+=cos
        z+=seny
        SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
        GET_OBJECT_COORDINATES objeto x y z
        POINT_CAMERA_AT_POINT x y z 2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_L
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    z-=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        z-=@0.5
                    ELSE
                        z-=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
    ENDIF
    
    IF IS_KEY_JUST_PRESSED VK_KEY_D
    OR IS_KEY_JUST_PRESSED VK_RIGHT
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x+=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        x+=@0.5
                    ELSE
                        x+=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_A
    OR IS_KEY_JUST_PRESSED VK_LEFT
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x-=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        x-=@0.5
                    ELSE
                        x-=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_W
    OR IS_KEY_JUST_PRESSED VK_UP
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    y+=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        y+=@0.5
                    ELSE
                        y+=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_S
    OR IS_KEY_JUST_PRESSED VK_DOWN
                GET_OBJECT_COORDINATES objeto x y z
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    y-=@0.01
                ELSE
                    IF IS_KEY_PRESSED 16
                        y-=@0.5
                    ELSE
                        y-=@0.1
                    ENDIF
                ENDIF
                SET_OBJECT_COORDINATES objeto x y z
                x+=sen
                y+=cos
                z+=seny
                SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
                GET_OBJECT_COORDINATES objeto x y z
                POINT_CAMERA_AT_POINT x y z 2
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_KEY_M
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    scale+=@0.005
                ELSE
                    IF IS_KEY_PRESSED 16
                        scale+=@0.1
                    ELSE
                        scale+=@0.01
                    ENDIF 
                ENDIF
                SET_OBJECT_SCALE objeto scale

    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_N
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    scale-=@0.005
                ELSE
                    IF IS_KEY_PRESSED 16
                        scale-=@0.1
                    ELSE
                        scale-=@0.01
                    ENDIF 
                ENDIF
                SET_OBJECT_SCALE objeto scale
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_KEY_E 
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x2+=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        x2+=@1.0
                    ELSE
                        x2+=@0.5
                    ENDIF 
                ENDIF

                SET_OBJECT_ROTATION objeto angle axis x2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_Q 
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    x2-=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        x2-=@1.0
                    ELSE
                        x2-=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_KEY_J
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    axis+=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        axis+=@1.0
                    ELSE
                        axis+=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_G
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    axis-=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        axis-=@1.0
                    ELSE
                        axis-=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_KEY_Y
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    angle+=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        angle+=@1.0
                    ELSE
                        angle+=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2
    ENDIF
    IF IS_KEY_JUST_PRESSED VK_KEY_H
                GET_OBJECT_HEADING objeto x2
                IF IS_KEY_PRESSED 17 //17 control, 16 shift
                    angle-=@0.1
                ELSE
                    IF IS_KEY_PRESSED 16
                        angle-=@1.0
                    ELSE
                        angle-=@0.5
                    ENDIF 
                ENDIF
                SET_OBJECT_ROTATION objeto angle axis x2 //angle=xrot, axis=yrot, x2=angle
    ENDIF
    RETURN
menuobj:
    selected=0
    WHILE TRUE
        WAIT 0
        DRAW_STRING "Press ~y~Z~w~ to close the menu" DRAW_EVENT_BEFORE_HUD 500.0 380.0 0.3 0.7 1 1

        DRAW_RECT 115.0 30.0 170.0 30.0 231 85 21 200
        
        DRAW_RECT 115.0 115.0 170.0 150.0 0 0 0 200

        DRAW_RECT 115.0 45.0 170.0 5.0 180 180 180 255

        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.40 1.61
        SET_TEXT_COLOUR 230 230 230 255
        DISPLAY_TEXT 85.0 22.0 RCF-OBW

    //reset rot
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
        IF selected=0
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 60.0 RCO-RRO
    //reset scale
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
        IF selected=1
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 85.0 RCO-RSC
    //enter id
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
        IF selected=2
            SET_TEXT_COLOUR 230 230 150 240
            PRINT_FORMATTED_NOW "Enter the ID of an object. Works with custom/added objects as well." 100
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 110.0 RCO-CID
    //exit
        SET_TEXT_FONT 1
        SET_TEXT_EDGE 1 0 0 0 255
        SET_TEXT_SCALE 0.4 1.6//SET_TEXT_SCALE 0.45 1.66
        IF selected=3
            SET_TEXT_COLOUR 230 230 150 240
        ELSE
            SET_TEXT_COLOUR 230 230 230 50
        ENDIF
        SET_TEXT_BACKGROUND 1
        DISPLAY_TEXT 50.0 135.0 RCFEXIT
    //controls
        IF IS_KEY_JUST_PRESSED VK_KEY_Z
            RETURN
        ENDIF
        IF IS_KEY_JUST_PRESSED VK_DOWN
        OR IS_KEY_JUST_PRESSED VK_RIGHT
        OR IS_KEY_JUST_PRESSED VK_KEY_S
            IF selected=3
                selected=0
            ELSE
                selected+=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_UP
        OR IS_KEY_JUST_PRESSED VK_LEFT
        OR IS_KEY_JUST_PRESSED VK_KEY_W
            IF selected=0
                selected=3
            ELSE
                selected-=1
            ENDIF
            GET_AUDIO_SFX_VOLUME angle
            //WRITE_MEMORY coords 4 selected 0
            CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 angle 3
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_SPACE
            SWITCH selected
                CASE 0
                    axis=0.0
                    angle=0.0
                    SET_OBJECT_ROTATION objeto 0.0 0.0 0.0
                    BREAK
                CASE 1
                    scale=1.0
                    SET_OBJECT_SCALE objeto 1.1
                    SET_OBJECT_SCALE objeto 1.0
                    BREAK
                CASE 2
                    CLEO_CALL GetObjString 0 id
                    GET_MODEL_TYPE id offs
                    IF NOT offs=MODEL_TYPE_VEHICLE
                    AND NOT offs=MODEL_TYPE_INVALID
                    AND NOT offs=MODEL_TYPE_PED
                    AND NOT offs=MODEL_TYPE_WEAPON
                        GET_OBJECT_HEADING objeto x2
                        DELETE_OBJECT objeto
                        CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
                        SET_OBJECT_COLLISION objeto 0
                        SET_OBJECT_ROTATION objeto angle axis x2
                        SET_OBJECT_SCALE objeto scale
                        PRINT_FORMATTED_NOW "~b~%i~w~ loaded!" 1000 id
                    ELSE
                        IF NOT id=-1
                            PRINT_FORMATTED_NOW "~y~%i~r~ is not a valid object ID!" 1000 id
                            WAIT 1000
                        ENDIF
                    ENDIF
                    BREAK
                CASE 3
                    SET_TEXT_DRAW_BEFORE_FADE 0
                    USE_TEXT_COMMANDS 0
                    idd="CLOSE_NOW"
                    RETURN
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE
    RETURN
    
saveObjIni:
    GET_OBJECT_COORDINATES objeto x y z
    GET_OBJECT_HEADING objeto x2
    GET_LABEL_POINTER Coords offs
    STRING_FORMAT offs "%i %f %f %f %f %f %f %f" id x y z x2 angle axis scale
    GET_LIST_SIZE lista selected
    STRING_FORMAT idd "%i" selected
    WRITE_STRING_TO_INI_FILE $offs $filename "Objects" $idd 
RETURN
//

showhelp:
WAIT 0
WHILE TRUE
    WAIT 0
    DRAW_STRING "Press ~y~B~w~ to close" DRAW_EVENT_BEFORE_HUD 500.0 395.0 0.3 0.7 1 1

    DRAW_RECT 390.0 235.0 380.0 180.0 0 0 0 50

    DRAW_STRING "~y~Controls:" DRAW_EVENT_BEFORE_HUD 210.0 160.0 0.3 0.7 1 1
    
    DRAW_STRING " Press ~y~W,A,S,D~w~ keys (or arrows) to move the object.~n~ Press ~y~Q~w~ and ~y~E~w~ keys to change object heading." DRAW_EVENT_BEFORE_HUD 210.0 175.0 0.3 0.7 1 1
    //DRAW_STRING " Press ~y~Q~w~ and ~y~E~w~ keys to change object heading." DRAW_EVENT_BEFORE_HUD 300.0 260.0 0.3 0.7 1 1
    DRAW_STRING " Press ~y~Y,G,H,J~w~ keys to rotate the object.~n~ Press ~y~P~w~ and ~y~L~w~ keys to move the object up/down." DRAW_EVENT_BEFORE_HUD 210.0 205.0 0.3 0.7 1 1
    //DRAW_STRING "- Press ~y~P~w~ and ~y~L~w~ keys to move the object up/down." DRAW_EVENT_BEFORE_HUD 300.0 290.0 0.3 0.7 1 1
    DRAW_STRING " Press ~y~M~w~ and ~y~N~w~ keys to make the object bigger/smaller." DRAW_EVENT_BEFORE_HUD 210.0 235.0 0.3 0.7 1 1
    DRAW_STRING " Press ~y~Space~w~ to place the object.~n~ Press ~y~Backspace~w~ to delete the latest placed object." DRAW_EVENT_BEFORE_HUD 210.0 250.0 0.3 0.7 1 1
    //DRAW_STRING "- Press ~y~Space~w~ to place the object." DRAW_EVENT_BEFORE_HUD 300.0 335.0 0.3 0.7 1 1
    DRAW_STRING " You can use the mouse to move the camera, and scroll down/up to change zoom." DRAW_EVENT_BEFORE_HUD 210.0 280.0 0.3 0.7 1 1
    DRAW_STRING " Keep pressed ~y~Shift~w~ to move/rotate/scale faster, and keep pressed ~y~Ctrl~w~ to make it slower." DRAW_EVENT_BEFORE_HUD 210.0 290.0 0.3 0.7 1 1
    //DRAW_STRING "~yNotes:" DRAW_EVENT_BEFORE_HUD 300.0 380.0 0.3 0.7 1 1
    //DRAW_STRING "- Changing the scale of the object can cause problems with the hitbox." DRAW_EVENT_BEFORE_HUD 300.0 395.0 0.3 0.7 1 1

    IF IS_KEY_JUST_PRESSED VK_KEY_B
        RETURN
    ENDIF
ENDWHILE
RETURN
//
loadObjects:
    GET_LABEL_POINTER Coords2 offs
    selected=1
    STRING_FORMAT idd "%i" selected
    WHILE READ_STRING_FROM_INI_FILE $filename "Objects" $idd offs
    AND NOT IS_STRING_EQUAL $offs "DELETED" 10 0 "zzz"
        WAIT 0
        SCAN_STRING $offs "%i %f %f %f %f %f %f %f" menu id x y z x2 angle axis scale
        CREATE_OBJECT_NO_SAVE id x y z 0 0 objeto
        SET_OBJECT_SCALE objeto scale
        SET_OBJECT_ROTATION objeto angle axis x2
        LIST_ADD lista objeto
        selected+=1
        STRING_FORMAT idd "%i" selected
        GET_OBJECT_MODEL objeto menu
        MARK_MODEL_AS_NO_LONGER_NEEDED menu
    ENDWHILE
CLEO_RETURN 0
//
objectsedit:
selected=0
zoom=10.0
GET_LIST_VALUE_BY_INDEX lista 0 objeto
GOSUB prepplayer
WHILE TRUE
    GET_OBJECT_COORDINATES objeto x y z
    DRAW_STRING "Press ~y~Up/Down~w~ arrows to navigate" DRAW_EVENT_BEFORE_HUD 500.0 380.0 0.3 0.7 1 1
    DRAW_STRING "Press ~y~Space~w~ to edit this object" DRAW_EVENT_BEFORE_HUD 500.0 395.0 0.3 0.7 1 1
    DRAW_STRING "Press ~y~Supr~w~ to delete this object" DRAW_EVENT_BEFORE_HUD 500.0 410.0 0.3 0.7 1 1
    DRAW_STRING "Press ~y~Z~w~ to exit" DRAW_EVENT_BEFORE_HUD 500.0 425.0 0.3 0.7 1 1

    GET_LABEL_POINTER Coords id
    STRING_FORMAT id "~b~X~w~=%f" x
    DRAW_STRING $id DRAW_EVENT_BEFORE_HUD 60.0 280.0 0.3 0.7 1 1
    STRING_FORMAT id "~b~Y~w~=%f" y
    DRAW_STRING $id DRAW_EVENT_BEFORE_HUD 60.0 295.0 0.3 0.7 1 1
    STRING_FORMAT id "~b~Z~w~=%f" z
    DRAW_STRING $id DRAW_EVENT_BEFORE_HUD 60.0 310.0 0.3 0.7 1 1
    WAIT 0
    GET_ACTIVE_CAMERA_COORDINATES x2 y2 z2
    SET_CHAR_COORDINATES scplayer x2 y2 z2
    GET_PC_MOUSE_MOVEMENT x2 y2
    IF x2>10.0
        x2=10.0
    ENDIF
    IF y2>10.0
        y2=10.0
    ENDIF
    
    
    camx+=x2
    camy+=y2

    IF camx>360.0
        camx=0.0
    ENDIF
    IF camx<0.0
        camx=360.0
    ENDIF

    IF camy>90.0
        camy=90.0
    ENDIF
    IF camy<-90.0
        camy=-90.0
    ENDIF
    
    

    SIN camx sen
    COS camx cos
    sen*=zoom
    cos*=zoom

    //PRINT_FORMATTED_NOW "y=%f x=%f sen=%f cos=%f" 100 camy camx sen cos

    SIN camy seny
    seny-=0.0
    seny*=zoom
    //GET_TARGET_BLIP_COORDS x y z
    //PRINT_FORMATTED_NOW "%f %f %f" 100 x y z
    GET_OBJECT_COORDINATES objeto x y z
    x+=sen
    y+=cos
    z+=seny
    
    SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0

    GET_OBJECT_COORDINATES objeto x y z
    POINT_CAMERA_AT_POINT x y z 2

    IF IS_MOUSE_WHEEL_UP
        zoom-=0.5
    ENDIF
    IF IS_MOUSE_WHEEL_DOWN
        zoom+=0.5
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_UP
    OR IS_KEY_JUST_PRESSED VK_LEFT
    OR IS_KEY_JUST_PRESSED VK_KEY_W
        selected+=1
        GET_LIST_SIZE lista offs
        offs-=1
        IF NOT selected > offs
            GET_LIST_VALUE_BY_INDEX lista selected objeto
        ELSE
            selected=0
            GET_LIST_VALUE_BY_INDEX lista selected objeto
        ENDIF
    ENDIF


    IF IS_KEY_JUST_PRESSED VK_DOWN
    OR IS_KEY_JUST_PRESSED VK_RIGHT
    OR IS_KEY_JUST_PRESSED VK_KEY_S
        IF selected=0
            GET_LIST_SIZE lista selected
        ENDIF
        selected-=1
        GET_LIST_VALUE_BY_INDEX lista selected objeto
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_SPACE
        GOSUB editarobj
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_DELETE
        id=selected
        GOSUB deletethis
        GOSUB updateini
        selected=id
        IF selected=0
            selected=0
        ELSE
            selected-=1
        ENDIF
        GET_LIST_SIZE lista offs
        IF NOT offs=0
            GET_LIST_VALUE_BY_INDEX lista selected objeto
        ELSE
            PRINT_FORMATTED_NOW "All objects were deleted." 500
            SET_PLAYER_CONTROL_PAD_MOVEMENT 0 1
            FREEZE_CHAR_POSITION scplayer 0
            SET_CHAR_COLLISION scplayer 1
            SET_CHAR_VISIBLE scplayer 1
            RESTORE_CAMERA
            GET_LABEL_POINTER PlayerCoords offs
            SCAN_STRING $offs "%f %f %f" selected x y z
            SET_CHAR_COORDINATES scplayer x y z
            GOTO menuobjeto
        ENDIF
    ENDIF

    IF IS_KEY_JUST_PRESSED VK_KEY_Z
        SET_PLAYER_CONTROL_PAD_MOVEMENT 0 1
        FREEZE_CHAR_POSITION scplayer 0
        SET_CHAR_COLLISION scplayer 1
        SET_CHAR_VISIBLE scplayer 1
        RESTORE_CAMERA
        GET_LABEL_POINTER PlayerCoords2 offs
        SCAN_STRING $offs "%f %f %f" selected x y z
        SET_CHAR_COORDINATES scplayer x y z
        GOTO menuobjeto
    ENDIF
ENDWHILE
GOTO menuobjeto
//
editarobj:
selected+=1
STRING_FORMAT idd "%i" selected
selected-=1
GET_LABEL_POINTER Coords offs
READ_STRING_FROM_INI_FILE $filename "Objects" $idd offs
SCAN_STRING $offs "%i %f %f %f %f %f %f %f" offs id x y z x2 angle axis scale
WHILE TRUE
    WAIT 0
    DRAW_STRING "Press ~y~Space~w~ to confirm" DRAW_EVENT_BEFORE_HUD 500.0 395.0 0.3 0.7 1 1
    GET_LABEL_POINTER Coords offs
    GET_OBJECT_COORDINATES objeto x y z
    STRING_FORMAT offs "~b~X~w~=%f" x
    DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 280.0 0.3 0.7 1 1
    STRING_FORMAT offs "~b~Y~w~=%f" y
    DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 295.0 0.3 0.7 1 1
    STRING_FORMAT offs "~b~Z~w~=%f" z
    DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 310.0 0.3 0.7 1 1
    STRING_FORMAT offs "~b~Scale~w~=%f" scale
    DRAW_STRING $offs DRAW_EVENT_BEFORE_HUD 60.0 325.0 0.3 0.7 1 1

        GET_ACTIVE_CAMERA_COORDINATES x2 y2 z2
        SET_CHAR_COORDINATES scplayer x2 y2 z2
        GET_PC_MOUSE_MOVEMENT x2 y2
        IF x2>10.0
            x2=10.0
        ENDIF
        IF y2>10.0
            y2=10.0
        ENDIF
        
        
        camx+=x2
        camy+=y2

        IF camx>360.0
            camx=0.0
        ENDIF
        IF camx<0.0
            camx=360.0
        ENDIF

        IF camy>90.0
            camy=90.0
        ENDIF
        IF camy<-90.0
            camy=-90.0
        ENDIF
        
        

        SIN camx sen
        COS camx cos
        sen*=zoom
        cos*=zoom

        //PRINT_FORMATTED_NOW "y=%f x=%f sen=%f cos=%f" 100 camy camx sen cos

        SIN camy seny
        seny-=0.0
        seny*=zoom
        //GET_TARGET_BLIP_COORDS x y z
        //PRINT_FORMATTED_NOW "%f %f %f" 100 x y z
        GET_OBJECT_COORDINATES objeto x y z
        x+=sen
        y+=cos
        z+=seny
        
        SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
       
        GET_OBJECT_COORDINATES objeto x y z
        POINT_CAMERA_AT_POINT x y z 2

        IF IS_MOUSE_WHEEL_UP
            zoom-=0.5
        ENDIF
        IF IS_MOUSE_WHEEL_DOWN
            zoom+=0.5
        ENDIF
        
        IF IS_KEY_PRESSED VK_KEY_P
            
            GET_OBJECT_COORDINATES objeto x y z

            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                z+=0.01
            ELSE
                IF IS_KEY_PRESSED 16
                    z+=0.5
                ELSE
                    z+=0.1
                ENDIF
            ENDIF
            
            SET_OBJECT_COORDINATES objeto x y z
            x+=sen
            y+=cos
            z+=seny
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OBJECT_COORDINATES objeto x y z
            POINT_CAMERA_AT_POINT x y z 2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_L
            GET_OBJECT_COORDINATES objeto x y z
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                z-=0.01
            ELSE
                IF IS_KEY_PRESSED 16
                    z-=0.5
                ELSE
                    z-=0.1
                ENDIF
            ENDIF
            SET_OBJECT_COORDINATES objeto x y z
            x+=sen
            y+=cos
            z+=seny
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OBJECT_COORDINATES objeto x y z
            POINT_CAMERA_AT_POINT x y z 2
        ENDIF
        
        IF IS_KEY_PRESSED VK_KEY_D
        OR IS_KEY_PRESSED VK_RIGHT
            GET_OBJECT_COORDINATES objeto x y z
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                x+=0.01
            ELSE
                IF IS_KEY_PRESSED 16
                    x+=0.5
                ELSE
                    x+=0.1
                ENDIF
            ENDIF
            SET_OBJECT_COORDINATES objeto x y z
            x+=sen
            y+=cos
            z+=seny
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OBJECT_COORDINATES objeto x y z
            POINT_CAMERA_AT_POINT x y z 2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_A
        OR IS_KEY_PRESSED VK_LEFT
            GET_OBJECT_COORDINATES objeto x y z
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                x-=0.01
            ELSE
                IF IS_KEY_PRESSED 16
                    x-=0.5
                ELSE
                    x-=0.1
                ENDIF
            ENDIF
            SET_OBJECT_COORDINATES objeto x y z
            x+=sen
            y+=cos
            z+=seny
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OBJECT_COORDINATES objeto x y z
            POINT_CAMERA_AT_POINT x y z 2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_W
        OR IS_KEY_PRESSED VK_UP
            GET_OBJECT_COORDINATES objeto x y z
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                y+=0.01
            ELSE
                IF IS_KEY_PRESSED 16
                    y+=0.5
                ELSE
                    y+=0.1
                ENDIF
            ENDIF
            SET_OBJECT_COORDINATES objeto x y z
            x+=sen
            y+=cos
            z+=seny
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OBJECT_COORDINATES objeto x y z
            POINT_CAMERA_AT_POINT x y z 2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_S
        OR IS_KEY_PRESSED VK_DOWN
            GET_OBJECT_COORDINATES objeto x y z
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                y-=0.01
            ELSE
                IF IS_KEY_PRESSED 16
                    y-=0.5
                ELSE
                    y-=0.1
                ENDIF
            ENDIF
            SET_OBJECT_COORDINATES objeto x y z
            x+=sen
            y+=cos
            z+=seny
            SET_FIXED_CAMERA_POSITION x y z 0.0 0.0 0.0
            GET_OBJECT_COORDINATES objeto x y z
            POINT_CAMERA_AT_POINT x y z 2
        ENDIF

        IF IS_KEY_PRESSED VK_KEY_M
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                scale+=0.005
            ELSE
                IF IS_KEY_PRESSED 16
                    scale+=0.1
                ELSE
                    scale+=0.01
                ENDIF 
            ENDIF
            SET_OBJECT_SCALE objeto scale
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_N
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                scale-=0.005
            ELSE
                IF IS_KEY_PRESSED 16
                    scale-=0.1
                ELSE
                    scale-=0.01
                ENDIF 
            ENDIF
            SET_OBJECT_SCALE objeto scale
        ENDIF

        IF IS_KEY_PRESSED VK_KEY_E 
            GET_OBJECT_HEADING objeto x2
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                x2+=0.1
            ELSE
                IF IS_KEY_PRESSED 16
                    x2+=1.0
                ELSE
                    x2+=0.5
                ENDIF 
            ENDIF
            
            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_Q 
            GET_OBJECT_HEADING objeto x2
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                x2-=0.1
            ELSE
                IF IS_KEY_PRESSED 16
                    x2-=1.0
                ELSE
                    x2-=0.5
                ENDIF 
            ENDIF
            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF

        IF IS_KEY_PRESSED VK_KEY_J
            GET_OBJECT_HEADING objeto x2
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                axis+=0.1
            ELSE
                IF IS_KEY_PRESSED 16
                    axis+=1.0
                ELSE
                    axis+=0.5
                ENDIF 
            ENDIF
            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_G
            GET_OBJECT_HEADING objeto x2
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                axis-=0.1
            ELSE
                IF IS_KEY_PRESSED 16
                    axis-=1.0
                ELSE
                    axis-=0.5
                ENDIF 
            ENDIF
            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF

        IF IS_KEY_PRESSED VK_KEY_Y
            GET_OBJECT_HEADING objeto x2
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                angle+=0.1
            ELSE
                IF IS_KEY_PRESSED 16
                    angle+=1.0
                ELSE
                    angle+=0.5
                ENDIF 
            ENDIF
            SET_OBJECT_ROTATION objeto angle axis x2
        ENDIF
        IF IS_KEY_PRESSED VK_KEY_H
            GET_OBJECT_HEADING objeto x2
            IF IS_KEY_PRESSED 17 //17 control, 16 shift
                angle-=0.1
            ELSE
                IF IS_KEY_PRESSED 16
                    angle-=1.0
                ELSE
                    angle-=0.5
                ENDIF 
            ENDIF
            SET_OBJECT_ROTATION objeto angle axis x2 //angle=xrot, axis=yrot, x2=angle
        ENDIF

        IF IS_KEY_JUST_PRESSED VK_SPACE
            GET_LABEL_POINTER Coords offs
            GET_OBJECT_HEADING objeto x2
            STRING_FORMAT offs "%i %f %f %f %f %f %f %f" id x y z x2 angle axis scale
            selected+=1
            STRING_FORMAT idd "%i" selected
            selected-=1
            WRITE_STRING_TO_INI_FILE $offs $filename "Objects" $idd
            RETURN
        ENDIF

ENDWHILE
RETURN
//
updateini:
GET_LIST_SIZE lista id
selected+=1
WHILE NOT selected>id
    WAIT 0
    PRINT_FORMATTED_NOW "~y~Updating..." 50
    GET_LABEL_POINTER Coords offs
    selected+=1
    STRING_FORMAT idd "%i" selected
    READ_STRING_FROM_INI_FILE $filename "Objects" $idd offs
    selected-=1
    STRING_FORMAT idd "%i" selected
    WRITE_STRING_TO_INI_FILE $offs $filename "Objects" $idd
    selected+=1
ENDWHILE
WAIT 0
STRING_FORMAT idd "%i" selected
GET_LABEL_POINTER Coords offs
STRING_FORMAT offs "DELETED"
WRITE_STRING_TO_INI_FILE $offs $filename "Objects" $idd
selected-=1
RETURN
//
deletethis:
    DELETE_OBJECT objeto
    LIST_REMOVE_INDEX lista selected
    /*selected+=1
    STRING_FORMAT idd "%i" selected
    selected-=1
    WRITE_STRING_TO_INI_FILE "DELETED" $filename "Objects" $idd*/
RETURN
//
deleteObjects:
    offs=0
    GET_LIST_SIZE lista selected
    IF NOT selected=0
        WHILE NOT offs>selected
            WAIT 0
            GET_LIST_VALUE_BY_INDEX lista offs objeto
            //MARK_OBJECT_AS_NO_LONGER_NEEDED objeto
            DELETE_OBJECT objeto
            offs+=1
        ENDWHILE
    ENDIF
    DELETE_LIST lista
CLEO_RETURN 0
//
stopobj:
    DELETE_OBJECT objeto
    SET_PLAYER_CONTROL_PAD_MOVEMENT 0 1
    FREEZE_CHAR_POSITION scplayer 0
    SET_CHAR_COLLISION scplayer 1
    SET_CHAR_VISIBLE scplayer 1
    IF IS_CHAR_IN_ANY_CAR scplayer
    OR IS_CHAR_IN_ANY_BOAT scplayer
    OR IS_CHAR_IN_FLYING_VEHICLE scplayer
        GET_CAR_CHAR_IS_USING scplayer car
        SET_CAR_VISIBLE car 1
        SET_CAR_COLLISION car 1
        FREEZE_CAR_POSITION car 0
        WARP_CHAR_INTO_CAR scplayer car
    ENDIF
    RESTORE_CAMERA
    GET_LABEL_POINTER PlayerCoords2 offs
    SCAN_STRING $offs "%f %f %f" selected x y z
    SET_CHAR_COORDINATES scplayer x y z
GOTO menuobjeto
}
{//escribir ID obj
LVAR_INT selected vk_key memory count
LVAR_TEXT_LABEL name test
    GetObjString:
        GET_LABEL_POINTER Coords selected
        WRITE_MEMORY selected 16 0x0 0
        memory = selected
        SET_PLAYER_CONTROL 0 FALSE
        READ_MEMORY 0x00969110 1 0 vk_key  
        WHILE IS_KEY_PRESSED vk_key 
            WAIT 0
        ENDWHILE
        PRINT_HELP_FORMATTED "Press ~y~Enter~w~ to confirm~n~Press ~y~Backspace~w~ to clear~n~ Press ~y~Z~w~ to cancel"
        WHILE NOT IS_KEY_PRESSED VK_RETURN // {vk_return} Confirm
        AND NOT (count >= 12)
            WAIT 0
            READ_MEMORY 0x00969110 1 0 vk_key
            IF IS_KEY_PRESSED vk_key
            OR IS_KEY_PRESSED VK_BACK
                IF IS_KEY_PRESSED VK_BACK // {vk_back} Clear
                    WRITE_MEMORY selected 16 0x0 0
                    memory = selected
                    count = 0
                ELSE
                    IF IS_KEY_JUST_PRESSED VK_KEY_Z
                        CLEO_RETURN 0 -1
                    ENDIF
                    WRITE_MEMORY memory 1 vk_key 0
                    memory ++
                    count ++
                ENDIF
                WHILE IS_KEY_PRESSED vk_key
                OR IS_KEY_PRESSED VK_BACK
                    WAIT 0 
                ENDWHILE
            ENDIF
            PRINT_FORMATTED_NOW ">~y~%s~w~<" 5200 $selected
            IF IS_KEY_JUST_PRESSED VK_KEY_Z
                CLEO_RETURN 0 -1
            ENDIF
        ENDWHILE
        IF SCAN_STRING $selected "%i" count selected
            WAIT 0
        ENDIF
    CLEO_RETURN 0 selected
}
{
    LVAR_INT entity
    LVAR_FLOAT x y z
    LVAR_INT pMatrix pCoord

    setEntityPosSimple:
    pMatrix = entity + 0x14
    READ_MEMORY pMatrix 4 0 pMatrix
    pCoord = pMatrix + 0x30
    WRITE_MEMORY pCoord 4 x 0
    pCoord += 0x4
    WRITE_MEMORY pCoord 4 y 0
    pCoord += 0x4
    WRITE_MEMORY pCoord 4 z 0
    CLEO_RETURN 0
}
script_end
Dumper:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //32
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //64
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //
ENDDUMP

Dumper2:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
ENDDUMP

RaceName:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //32
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //64
00 00 00 00 00 00 00 00 00 00 00 00 //76
ENDDUMP

Coords:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //128 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
ENDDUMP

Coords2:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //128 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
ENDDUMP

PlayerCoords:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
ENDDUMP

PlayerCoords2:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
ENDDUMP

IsCircuit:
DUMP
00 00 00 00
ENDDUMP

AutoCh:
DUMP
00 00 00 00 //x2
00 00 00 00 //y2
00 00 00 00 //offs
00 00 00 00 //exit/return
ENDDUMP

IsLive:
DUMP
00 00 00 00
ENDDUMP

PlayerInt:
DUMP
00 00 00 00
00 00 00 00
ENDDUMP

RaceOpts:
DUMP
00 00 00 00 //weather
00 00 00 00 //gravity
00 00 00 00 //wanted level
00 00 00 00 //traffic
00 00 00 00 //damage
00 00 00 00 //time
00 00 00 00 //is wl fixed
00 00 00 00 //mechanical damage
00 00 00 00 //is time fixed
ENDDUMP

Contadores:
DUMP
00 00 00 00 
00 00 00 00
ENDDUMP 

Pos:
DUMP
00 00 00 00 //player pos
00 00 00 00 //cantidad de oponentes superando al jugador
ENDDUMP

Auxiliar:
DUMP
00 00 00 00
00 00 00 00
00 00 00 00 //total checkpoints
00 00 00 00 //if 1st lap
ENDDUMP

Istring:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //22
ENDDUMP

Checkpoint:
DUMP
00 00 00 00 //start line checkpoint
00 00 00 00 //start line blip
00 00 00 00 //i-1 checkpoint
00 00 00 00 //i-1 blip
00 00 00 00 //i checkpoint
00 00 00 00 //i blip
ENDDUMP

OpponentsPositions:
DUMP
00 00 00 00 //first opp pos (not player)
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP

Opponents:
DUMP
00 00 00 00 //first opp pos (not player)//also first marker in create
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP

Opponentsch:
DUMP
00 00 00 00 //first opp checkp (not player)
00 00 00 00
ENDDUMP

Test:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
ENDDUMP
Opponentslap:
DUMP
00 00 00 00 //lap counter (player)
00 00 00 00 //lap counter (opp1)
00 00 00 00
ENDDUMP

Opponentschar:
DUMP
00 00 00 00 //not chars, cantidad de oponentes que finalizaron
00 00 00 00 //first opp char
ENDDUMP

TypeRace:
DUMP
00 00 00 00
ENDDUMP

Helper:
DUMP
00 00 00 00
00 00 00 00
ENDDUMP

Files:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00//first .ini
ENDDUMP

Filesn:
DUMP
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP

Aioptions:
DUMP
00 00 00 00  //AI Behaviour
00 00 00 00 //AI Speed
00 00 00 00 //Visibility
ENDDUMP

Totlaps:
DUMP
00 00 00 00
ENDDUMP

TotCheck:
DUMP
00 00 00 00
ENDDUMP
//
Customcate:
DUMP
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP

LapTime:
DUMP
00 00 00 00
ENDDUMP

IsPlayerCar:
DUMP
00 00 00 00
ENDDUMP

MenusHelp:
DUMP
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP
//
VehMods:
DUMP
00 00 00 00 //hood
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP