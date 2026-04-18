
// ==========================================
function tGet(data) { return data.map(b => ('0' + (b & 0xFF).toString(16)).slice(-2)).join(''); }

function fmGet(h) {
    const result = [];
    for (let i = 0; i < h.length; i += 2) { result.push(parseInt(h.substr(i, 2), 16)); }
    return result;
}

function mix(data) {
    const a = [...data];
    let l = 0, r = a.length - 1, t = true;
    while (l < r) {
        if (t) { [a[l], a[r]] = [a[r], a[l]]; l += 2; } 
        else { r -= 2; }
        t = !t;
    }
    return a;
}

function unmix(data) {
    const n = data.length, ops = [];
    let l = 0, r = n - 1, t = true;
    while (l < r) {
        if (t) { ops.push([l, r]); l += 2; } 
        else { r -= 2; }
        t = !t;
    }
    const a = [...data];
    for (let i = ops.length - 1; i >= 0; i--) { const [li, ri] = ops[i]; [a[li], a[ri]] = [a[ri], a[li]]; }
    return a;
}

function q0(data, k, s) {
    const a = [];
    for (let i = 0; i < data.length; i++) { a.push(((data[i] + s + (i * 3)) & 0xFF) ^ ((k + i * 11) & 0xFF)); }
    return tGet(mix(a));
}

// BUG FIXED: Pemisahan eksekusi XOR dan pengurangan
function q1(h, k, s) {
    const a = unmix(fmGet(h)), r = [];
    for (let i = 0; i < a.length; i++) {
        const ux = (a[i] & 0xFF) ^ ((k + i * 11) & 0xFF);
        r.push((ux - s - (i * 3)) & 0xFF);
    }
    return r;
}

function ccmH(data, key, iterations) {
    let result = [...key];
    for (let i = 0; i < iterations; i++) {
        const combined = result.concat([...data]);
        const obf = q0(combined, 124 + i, 13 + (i * 2));
        result = q1(obf, 124 + i, 13 + (i * 2));
        if (!result || result.length === 0) result = new Array(32).fill(0);
        for (let j = 0; j < result.length; j++) result[j] ^= (j * 7) & 0xFF;
        if (result.length > 1) { const first = result.shift(); result.push(first); }
    }
    if (result.length > 32) result = result.slice(0, 32);
    return new Uint8Array(result);
}

function gGcs(apiKey, timestamp, nonce, secretBytes) {
    const dataBytes = [];
    for (let i = 0; i < apiKey.length; i++) dataBytes.push(apiKey.charCodeAt(i));
    dataBytes.push(58); // ':'
    for (let i = 0; i < timestamp.length; i++) dataBytes.push(timestamp.charCodeAt(i));
    dataBytes.push(58); // ':'
    for (let i = 0; i < nonce.length; i++) dataBytes.push(nonce.charCodeAt(i));
    
    const hashResult = ccmH(new Uint8Array(dataBytes), new Uint8Array(secretBytes), 13);
    let hexResult = '';
    for (let i = 0; i < hashResult.length; i++) hexResult += ('0' + hashResult[i].toString(16)).slice(-2);
    return hexResult;
}
