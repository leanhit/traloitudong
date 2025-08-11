import moment from 'moment';

function removeAccents(str: string) {
    return str.normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .replace(/đ/g, 'd').replace(/Đ/g, 'D');
}

function filterDataFunction(inputStr: string, inputArr: []): [] {
    console.log("filterDataFunction", inputStr, inputArr);
    const temp = [];
    const searchTxt = removeAccents(inputStr);
    inputArr.forEach(item => {
        const searchIn = removeAccents(
            item?.botId + " " +
            item?.pageId + " " +
            item?.appSecret + " " 
        );
        if (searchIn.toLowerCase().includes(searchTxt.toLocaleLowerCase())) {
            temp.push(item);
        }
    });
    return temp;
}

function filterFunction(inputStr: string, inputArr: []): [] {
    const temp = [];
    inputArr.forEach(item => {
        const searchIn = 
            item?.botId + " " +
            item?.pageId + " " +
            item?.appSecret + " " 
        
        if (searchIn.includes(inputStr)) {
            temp.push(item);
        }
    });
    return temp;
}

function splitData(inputArr: [], pagination: {}): [] {
    let maxPage = 0;
    const startPoint = pagination.pageSize * (pagination.currentPage - 1);
    let temp = [];
    //calculate max number of pages
    if (pagination.totalItems % pagination.pageSize == 0) {
        maxPage = pagination.totalItems / pagination.pageSize;
    } else {
        maxPage = Math.floor(pagination.totalItems / pagination.pageSize) + 1;
    }

    //copy all items in block
    if (pagination.pageSize * pagination.currentPage < pagination.totalItems) {
        const endPoint = pagination.pageSize * pagination.currentPage;
        for (var i = startPoint; i < endPoint; i++) {
            temp.push(inputArr[i]);
        }
    } else {
        //copy last block
        if (pagination.currentPage == maxPage && pagination.currentPage != 1) {
            const endPoint = pagination.totalItems;
            for (var i = startPoint; i < endPoint; i++) {
                temp.push(inputArr[i]);
            }
        } //get all item
        else {
            temp = inputArr;
        }
    }

    return temp;
}

function formatDateTime(dateTime:Date){
    if (!dateTime) dateTime = new Date();
    return moment(dateTime).calendar(null, {
        sameDay: '[] HH:mm:ss',
        lastDay: '[Hôm qua] HH:mm:ss',
        nextDay: '[Ngày mai] HH:mm:ss',
        lastWeek: 'DD/MM/YYYY HH:mm:ss',
        sameElse: 'DD/MM/YYYY HH:mm:ss',
    });
}

export {
    filterFunction,
    filterDataFunction,
    splitData,
    formatDateTime
}