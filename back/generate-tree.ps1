# generate-tree.ps1

# Récupère tous les éléments en excluant les dossiers cachés et le dossier 'target'
Get-ChildItem -Recurse -Force | 
    Where-Object { 
        $_.Name -notmatch '^\.' -and 
        $_.FullName -notlike "*\target*" -and 
        $_.FullName -notlike "*\.*" 
    } | 
    ForEach-Object { $_.FullName -replace [regex]::Escape((Get-Location).Path), '' } | 
    Sort-Object | 
    Out-File tree.txt

Write-Host "Thee file tree of the project was generated in tree.txt"
