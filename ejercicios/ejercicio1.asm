ldi 0,00
stm 0,fc
ldm 1,fd
stm 0,fc
ldm 2,fd
jmp 1,CC
jmp 2,CC
ldi 3,70 //exp Mask
ldi 4,Fc // exceso exp complemento 2
and 5,1,3 // obtener exponente 1
rrr 5,4   // rotar exponenete 1
and 6,2,3  // obtener exponente 2
rrr 6,4    // rotar exponente 2
add 15,5,6   //suma de exponente 
add 15,15,4  // quito un excedente y guardo en r15
ldi 12,80
xor 10,1,2
and 14,10,12
ldi 3,00   //reseteo registro 3
ldi 4,00   //reseteo registro 4
ldi 5,0F
ldi 6,10
and 1,5,1
oor 1,6,1
and 2,5,2
oor 2,6,2
ldi 5,01
stm 0,EA
jmp 0,50 //********** pmult
ldm 5,EC  //-----------------------> mult
rrr 5,7
rrr 1,7
ldi 0,01
and 7,1,0
ldi 0,fe
and 1,1,0
ldm 0,EA
rrr 0,7
oor 0,0,7
stm 0,EA
and 6,2,5 //-----------------------> pmult 
stm 5,EC
ldi 0,20
jmp 5,94 //******* finmult
ldi 0,00
jmp 6,3A //******* mult
stm 5,EC
ldi 5,01
ldi 6,00
ldi 0,08
ldi 9,00
ldi 10,00
add 6,6,13 //sube contador para saber cuando terminar -------> Insuma
and 7,1,5   //toma el primer bit del primer sumando
and 8,4,5   // toma el primer bit del segundo sumando
xor 12,7,8  // suma
rrr 10,7    // rota carry
xor 13,12,10  //suma carry
and 11,7,8    // carry del actual
and 10,12,10  // carry del actual
oor 10,11,10  // carry total
oor 9,13,9   
rrr 5,07
ldi 13,01
jmp 6,84 // ******** Insumasec
jmp 0,68 // ******** Insuma
ldi 8,7c  // --------> Insumasec
ldm 0,EA                                                
add 3,0,3
rrr 10,7
add 3,3,10
stm 0,EA
cpy 9,4
jmp 0,3A //****** mult
ldi 0,00 // ---------> finmult
ldi 5,02
and 6,5,3
ldi 7,00
ldi 5,00
jmp 6,A8 //******* nomasexp
ldi 6,01
ldi 7,01
add 15,15,6
and 5,3,6 
ldi 13,01 //----------> nomasexp
add 7,7,13 
ldi 6,80
and 8,4,6
rrr 8,7
rrr 4,7
rrr 5,7
add 5,5,8
ldi 0,04
jmp 7,BE //************* fin
jmp 0,A8 //************* nomasexp
ldi 3,00 //------------> fin
rrr 15,4 
oor 3,3,15
oor 3,3,5
oor 3,14,3
ldi 13,00
stm 13,fe
stm 3,ff
end
ldi 3,00
jmp 0,c4