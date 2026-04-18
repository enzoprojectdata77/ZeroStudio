// ==========================================
// ALGORITMA KRIPTOGRAFI KMKZ
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

// ==========================================
// MODAL DECRYPT LOKAL
// ==========================================
window.openDecryptModal = function() { document.getElementById('decryptModalOverlay').classList.add('show'); document.getElementById('decryptSearchInput').value = ''; filterDecryptList(); };
window.closeDecryptModal = function() { document.getElementById('decryptModalOverlay').classList.remove('show'); };
window.filterDecryptList = function() {
    const query = document.getElementById('decryptSearchInput').value.toLowerCase(); const items = document.querySelectorAll('#decryptListContainer .vpn-list-item'); let count = 0;
    items.forEach(item => { const name = item.getAttribute('data-name').toLowerCase(); const id = item.getAttribute('data-id').toLowerCase();
        if (name.includes(query) || id.includes(query)) { item.style.display = 'flex'; count++; } else { item.style.display = 'none'; }
    }); document.getElementById('decryptCountText').innerText = `${count} configs available`;
};
window.renderDecryptList = function() {
    const listContainer = document.getElementById('decryptListContainer');
    if (!DECRYPT_FILE_TYPES.length) { listContainer.innerHTML = `<div style="text-align:center; padding:20px;">No configuration data</div>`; return; }
    let html = '';
    DECRYPT_FILE_TYPES.forEach(file => {
        const isSelected = selectedFileType === file.id ? 'selected' : '';
        html += `<div class="vpn-list-item ${isSelected}" data-id="${file.id}" data-name="${file.name}" onclick="selectDecryptType('${file.id}', '${file.name}', '${file.icon}')">
                    <div class="vpn-info"><img src="${file.icon}" onerror="this.src='https://via.placeholder.com/40'" alt="icon"><span>${file.name}</span></div>
                    <i data-lucide="check" class="check-icon"></i>
                </div>`;
    }); listContainer.innerHTML = html; document.getElementById('decryptCountText').innerText = `${DECRYPT_FILE_TYPES.length} configs available`; lucide.createIcons();
};
window.selectDecryptType = function(id, name, iconUrl, isAutoDetect = false) {
    selectedFileType = id; selectedFileTypeName = name;
    document.getElementById('selectedDecryptDisplay').innerHTML = iconUrl ? `<img src="${iconUrl}" onerror="this.src='https://via.placeholder.com/44'" style="width: 44px; height: 44px; border-radius: 12px; object-fit: cover; background: var(--surface-container-high);"><span>${name}</span>` : `<div class="file-icon-placeholder"><i data-lucide="file-code"></i></div><span>${name}</span>`;
    document.getElementById('autoDetectBadge').style.display = isAutoDetect ? 'inline-flex' : 'none'; closeDecryptModal(); lucide.createIcons();
};

window.handleDecrypt = async function() {
    const content = document.getElementById('encryptedContent').value.trim();
    if (!content) { showNotification(`<i data-lucide="alert-circle"></i> ${t('error_empty')}`, 'warning'); return; }
    if (!selectedFileType) { showNotification(`<i data-lucide="alert-circle"></i> Select config type!`, 'warning'); return; }
    
    const btn = document.getElementById('decryptBtn'); 
    if (btn.disabled) return;
    const originalText = btn.innerHTML; 
    btn.innerHTML = `<div class="spinner-small" style="margin-right:8px;"></div> PROCESSING...`; 
    btn.disabled = true;
    
    try {
        const pa = "RAHASIA_HMAC", r = "_SUPER_SULIT", te = "_DITEBAK_WINDOW"; 
        const VALUE_WINDOW = pa + r + te;
        const VALUE_DOCUMENT = "ENZO_XD"; 
        
        const timestampStr = Math.floor(Date.now() / 1000).toString();
        const nonce = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
        const secretBytes = Array.from(new TextEncoder().encode(VALUE_WINDOW));
        
        const signature = gGcs(VALUE_DOCUMENT, timestampStr, nonce, secretBytes);
        
        const _s0 = "yeK-IPA-X".split("").reverse().join("");
        const _s1 = "pmatsemiT-X".split("").reverse().join("");
        const _s2 = "erutangiS-ZKMK-X".split("").reverse().join(""); 
        const _nonce = "ecnoN-X".split("").reverse().join("");        
        const _s3 = "tpeccA".split("").reverse().join("");
        const _s4 = "epuT-tnetnoC".split("").reverse().join("").replace('u', 'y');
        
        const hCfg = {};
        hCfg[_s4] = 'application/json';
        hCfg[_s3] = 'application/json';
        hCfg[_s0] = VALUE_DOCUMENT;
        hCfg[_s1] = timestampStr;
        hCfg[_nonce] = nonce;
        hCfg[_s2] = signature;

        const response = await fetch('https://decodeconfigtxt.vercel.app/apidua/impor_deceypt', {
            method: 'POST', mode: 'cors', headers: hCfg,
            body: JSON.stringify({ content: content, file_type: `.${selectedFileType}` })
        });
        
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || data.detail || `HTTP Error ${response.status}`);
        
        if (data && (data.success === true || data.status === 'success')) {
            let finalDecryptedData = "";
            if (data.encrypted_result) {
                const gasResponse = await fetch(X_GAS_URL, {
                    method: 'POST', headers: { 'Content-Type': 'text/plain;charset=utf-8' },
                    body: JSON.stringify({ action: 'process', encrypted: data.encrypted_result })
                });
                const gasData = await gasResponse.json();
                if (gasData.status === 'success') finalDecryptedData = gasData.data;
                else throw new Error("Failed");
            } else {
                finalDecryptedData = data.decrypted || data.data || data.result || "No data";
            }

            try { 
                if (typeof finalDecryptedData === 'string' && (finalDecryptedData.trim().startsWith('{') || finalDecryptedData.trim().startsWith('['))) { 
                    finalDecryptedData = JSON.parse(finalDecryptedData); 
                } 
                decryptResult = typeof finalDecryptedData === 'object' ? JSON.stringify(finalDecryptedData, null, 2) : finalDecryptedData; 
            } catch (e) { decryptResult = finalDecryptedData; }
            
            document.getElementById('inputArea').classList.add('hidden'); 
            document.getElementById('resultArea').classList.add('show'); 
            document.getElementById('resultContent').innerText = decryptResult; 
            showNotification(`<i data-lucide="check-circle"></i> ${t('decrypt_success')}`, 'success');
        } else { throw new Error('Failed'); }
    } catch (error) { 
        showNotification(`Decryption Failed!`, 'error'); 
        document.getElementById('inputArea').classList.add('hidden');
        document.getElementById('resultContent').innerText = "Error: Cannot process the configuration.\nMake sure the format matches your payload and try again.";
        document.getElementById('resultArea').classList.add('show');
    } finally { 
        btn.innerHTML = originalText; btn.disabled = false; lucide.createIcons(); 
    }
};

// ==========================================
// MODAL MASTER DECRYPT (TH DECODE)
// ==========================================
window.openVpnModal = function() { document.getElementById('vpnModalOverlay').classList.add('show'); document.getElementById('vpnSearchInput').value = ''; filterVpnList(); };
window.closeVpnModal = function() { document.getElementById('vpnModalOverlay').classList.remove('show'); };
window.filterVpnList = function() {
    const query = document.getElementById('vpnSearchInput').value.toLowerCase(); const items = document.querySelectorAll('#vpnListContainer .vpn-list-item'); let count = 0;
    items.forEach(item => { const name = item.getAttribute('data-name').toLowerCase(); if (name.includes(query)) { item.style.display = 'flex'; count++; } else { item.style.display = 'none'; } });
    document.getElementById('vpnCountText').innerText = `${count} VPNs available`;
};
window.renderMasterVPNList = function() {
    const listContainer = document.getElementById('vpnListContainer');
    if (!MASTER_VPN_LIST.length) { listContainer.innerHTML = `<div style="text-align:center; padding:20px;">No VPN available</div>`; return; }
    let html = '';
    MASTER_VPN_LIST.forEach(vpn => {
        const isSelected = selectedMasterVPN === vpn.name ? 'selected' : '';
        html += `<div class="vpn-list-item ${isSelected}" data-name="${vpn.name}" onclick="selectMasterVPN('${vpn.name}', '${vpn.icon}')">
                    <div class="vpn-info"><img src="${vpn.icon}" onerror="this.src='https://via.placeholder.com/40'" alt="icon"><span>${vpn.name}</span></div>
                    <i data-lucide="check" class="check-icon"></i>
                </div>`;
    }); listContainer.innerHTML = html; document.getElementById('vpnCountText').innerText = `${MASTER_VPN_LIST.length} VPNs available`; lucide.createIcons();
};
window.selectMasterVPN = function(vpnName, vpnIcon) {
    selectedMasterVPN = vpnName; document.getElementById('selectedVpnDisplay').innerHTML = `<img src="${vpnIcon}" onerror="this.src='https://via.placeholder.com/44'" style="width: 44px; height: 44px; border-radius: 12px; object-fit: cover; background: var(--surface-container-high);"><span>${vpnName}</span>`;
    document.getElementById('masterDecryptBtn').disabled = false; document.getElementById('masterResultArea').style.display = 'none'; masterResult = ''; renderMasterVPNList(); closeVpnModal();
};

window.handleMasterDecrypt = async function() {
    if (!selectedMasterVPN || isMasterDecrypting) return; 
    isMasterDecrypting = true;
    
    const btn = document.getElementById('masterDecryptBtn'); 
    const originalText = btn.innerHTML; 
    btn.innerHTML = `<div class="spinner-small" style="margin-right:8px;"></div> PROCESSING...`; 
    btn.disabled = true;
    
    try {
        const pa = "RAHASIA_HMAC", r = "_SUPER_SULIT", te = "_DITEBAK_WINDOW"; 
        const VALUE_WINDOW = pa + r + te;
        const VALUE_DOCUMENT = "ENZO_XD"; 
        
        const timestampStr = Math.floor(Date.now() / 1000).toString();
        const nonce = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
        const secretBytes = Array.from(new TextEncoder().encode(VALUE_WINDOW));
        
        const signature = gGcs(VALUE_DOCUMENT, timestampStr, nonce, secretBytes);
        
        const _s0 = "yeK-IPA-X".split("").reverse().join("");
        const _s1 = "pmatsemiT-X".split("").reverse().join("");
        const _s2 = "erutangiS-ZKMK-X".split("").reverse().join(""); 
        const _nonce = "ecnoN-X".split("").reverse().join("");        
        const _s3 = "tpeccA".split("").reverse().join("");
        const _s4 = "epuT-tnetnoC".split("").reverse().join("").replace('u', 'y');
        
        const hCfg = {};
        hCfg[_s4] = 'application/json';
        hCfg[_s3] = 'application/json';
        hCfg[_s0] = VALUE_DOCUMENT;
        hCfg[_s1] = timestampStr;
        hCfg[_nonce] = nonce;
        hCfg[_s2] = signature;

        const response = await fetch(DECRYPT_MASTER_URL, { 
            method: 'POST', headers: hCfg, body: JSON.stringify({ vpn_name: selectedMasterVPN }) 
        });
        
        const data = await response.json(); 
        if (!response.ok) throw new Error(data.detail || data.error || `HTTP Error ${response.status}`);
        if (data.error) throw new Error(data.error);
        
        if (data.status === 'success' && data.encrypted_result) {
            const gasResponse = await fetch(X_GAS_URL, {
                method: 'POST', headers: { 'Content-Type': 'text/plain;charset=utf-8' },
                body: JSON.stringify({ action: 'process', encrypted: data.encrypted_result })
            });
            const gasData = await gasResponse.json();

            if (gasData.status === 'success') {
                let finalData = gasData.data;
                try {
                    if (typeof finalData === 'string' && (finalData.trim().startsWith('{') || finalData.trim().startsWith('['))) {
                        finalData = JSON.stringify(JSON.parse(finalData), null, 2); 
                    }
                } catch (e) {}
                masterResult = finalData;
                document.getElementById('masterResultContent').innerText = masterResult; 
                document.getElementById('masterResultArea').style.display = 'block'; 
                showNotification('Decryption successful!', 'success');
            } else { throw new Error('Failed'); }
        } else { throw new Error('Invalid format'); }
    } catch (error) { 
        showNotification(`Decryption Failed!`, 'error'); 
        document.getElementById('masterResultContent').innerText = "Error: Cannot process the configuration.\nMake sure the selected VPN app matches your payload and try again.";
        document.getElementById('masterResultArea').style.display = 'block'; 
    } finally { 
        isMasterDecrypting = false; btn.innerHTML = originalText; btn.disabled = false; lucide.createIcons(); 
    }
};
