const md5 = require("md5");
const bcrypt = require("bcrypt");
const fs = require("fs");

main();

async function main() {
    const passwords100 = readToplist("toplists/top100.txt"); 
    const passwords100k = readToplist("toplists/top100k.txt");

    var plainPasswords = generatePlainPasswords(passwords100, passwords100k);
    writeResults(plainPasswords, "results/passwords_for_weak.txt");
    const passwordsMD5 = hashWithMD5(plainPasswords);
    writeResults(passwordsMD5, "results/weak.csv");

    plainPasswords = generatePlainPasswords(passwords100, passwords100k);
    writeResults(plainPasswords, "results/passwords_for_strong.txt");
    const resultsBcrypt = await hashWithBcrypt(plainPasswords);
    var passwordsBcrypt = [];
    var csvBcrypt = [];
    resultsBcrypt.forEach(element => {
        passwordsBcrypt.push(element[1])
        csvBcrypt.push(element[0] + "," + element[1])
    });
    writeResults(passwordsBcrypt, "results/strong.csv");
    //writeResults(csvBcrypt, "results/strong_salt.csv");
}

function readToplist(path) {
    let toplist = [];
    try {
        toplist = fs.readFileSync(path, "utf8").split("\n");
    } catch (e) {
        console.log('File reading error:', e.stack);
    }
    return toplist;
}
function writeResults(results, path) {
    try {
        fs.writeFileSync(path, results.join("\n"), "utf8");
    } catch (e) {
        console.log('File writing error:', e.stack);
    }
}

function generatePlainPasswords(veryPopular, popular) {
    const passwords = [];
    for(let i=0; i<100000; i++) {
        let newPassword = "";
        if(i%20 === 0) {
            newPassword = generateStrongPassword();
        } else if (i%10 === 0) {
            newPassword = Buffer.from(veryPopular[Math.floor(Math.random()*veryPopular.length)]);
        } else {
            newPassword = Buffer.from(popular[Math.floor(Math.random()*popular.length)]);
        }
        passwords.push(newPassword);
    }
    return passwords;
}
function generateStrongPassword() {
    var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$%^&)><?_.,@";
    var byteCharacters = Buffer.from(characters);
    let passwordLength = 10 + Math.floor(Math.random() * 16);

    var result = [];
    for(let i=0; i<passwordLength; i++) {
        result.push(byteCharacters[Math.floor(Math.random()*byteCharacters.length)]);
    }
    
    //console.log(Buffer.from(result).toString());
    return Buffer.from(result);
}

function hashWithMD5(passwords) {
    let passwordsMD5 = [];
    passwords.forEach(element => {
        passwordsMD5.push(md5(element));
        console.log(md5(element));
    });
    return passwordsMD5;
}
async function hashWithBcrypt(passwords) {
    let resultsBcrypt = [];
    for(const password of passwords) {
        resultsBcrypt.push(genBcrypt(password));
    }
    resultsBcrypt = await Promise.all(resultsBcrypt);
    return resultsBcrypt;
}
async function genBcrypt(password) {
    const salt = await bcrypt.genSalt(10);
    const hash = await bcrypt.hash(password, salt);
    console.log(salt);
    console.log(hash);
    return [salt, hash];
}