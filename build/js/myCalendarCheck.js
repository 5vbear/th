/*
 * file : myCalendar.js
 *
 * 日付の有効性の確認を行うスクリプト
 *
 * $Id: 
 */


/*******************************************************************************
 * 日付有効確認処理.
 *
 * 指定した年月日が有効か判定する。
 *
 * @param   baseYear     確認する年
 * @param   baseMonth    確認する月
 * @param   baseDay      確認する日
 * @return               判定結果（0:有効 -1:無効）
 ******************************************************************************/
function checkDate( baseYear, baseMonth, baseDay ) {

    // 年が0001以降を正常とする。
    if ( baseYear < 1 ){
        return -1;
    }

    monthLastDay = 0;			// 月末日
    
    // 1月
    if( baseMonth == 01 ) {
        monthLastDay = 31;
    }

    // 2月
    else if( baseMonth == 02 ) {
        monthLastDay = 28;
        // 4で割り切れる年の場合
        if( ( baseYear % 4 ) == 0 ) {
            monthLastDay = 29;

            // 100で割り切れて400で割り切れない年は28日
            if( ( baseYear % 100 ) == 0 &&
                ( baseYear % 400 ) != 0 ) {
                monthLastDay = 28;
            }
        }
    }

    // 3月
    else if( baseMonth == 03 ) {
        monthLastDay = 31;
    }
    // 4月
    else if( baseMonth == 04 ) {
        monthLastDay = 30;
    }
    // 5月
    else if( baseMonth == 05 ) {
        monthLastDay = 31;
    }
    // 6月
    else if( baseMonth == 06 ) {
        monthLastDay = 30;
    }
    // 7月
    else if( baseMonth == 07 ) {
        monthLastDay = 31;
    }
    // 8月
    else if( baseMonth == 08 ) {
        monthLastDay = 31;
    }
    // 9月
    else if( baseMonth == 09 ) {
        monthLastDay = 30;
    }
    // 10月
    else if( baseMonth == 10 ) {
        monthLastDay = 31;
    }
    // 11月
    else if( baseMonth == 11 ) {
        monthLastDay = 30;
    }
    // 12月
    else if( baseMonth == 12 ) {
        monthLastDay = 31;
    }
    else{
        return -1;
    }

    // 指定日がその月に存在する場合
    if( 1 <= baseDay && baseDay <= monthLastDay ) {
        return 0;
    }
    return -1;
}

/*******************************************************************************
 * 日付確認処理.
 *
 * 指定した年月日が現在より未来か判定する。
 *
 * @param   baseYear     確認する年
 * @param   baseMonth    確認する月
 * @param   baseDay      確認する日
 * @return               判定結果（0:現在より過去 -1:現在より未来）
 ******************************************************************************/
function checkNowDate( baseYear, baseMonth, baseDay ) {

    dt = new Date();

    // 現在日付取得
    yyyy = dt.getFullYear();
    mm = dt.getMonth() + 1;
    dd = dt.getDate();

    // 現在より未来の場合
    if( ( yyyy < baseYear ) ||
        ( yyyy == baseYear && mm < baseMonth ) ||
        ( yyyy == baseYear && mm == baseMonth && dd < baseDay ) ) {

        return -1;
    }
    return 0;
}

/*******************************************************************************
 * 日付範囲確認処理.
 *
 * 検索開始年月日と検索終了年月日の範囲が有効か判定する。
 *
 * @param   startYear     検索開始年
 * @param   startManth    検索開始月
 * @param   startDay      検索開始日
 * @param   endYear       検索終了年
 * @param   endManth      検索終了月
 * @param   endDay        検索終了日
 * @return                判定結果（ 0:有効 -1:無効）
 ******************************************************************************/
function checkSearchDate( startYear, startManth, startDay,
                          endYear,   endMonth,   endDay ) {

    if(( startYear != "") && (endYear != "")){
       startDate = startYear + "" + startManth + "" + startDay;
       endDate = endYear + "" + endMonth + "" + endDay;

//    /* 2003/0507 Soft Abe 追加 Start */
//    if( (startDate != "") && (endDate != "")){ /
//    /* 2003/0507 Soft Abe 追加 Start */
    
       if( startDate > endDate ) {
           return -1;
       }
    }
    
    return 0;
}